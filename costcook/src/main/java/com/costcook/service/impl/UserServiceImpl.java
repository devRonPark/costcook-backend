package com.costcook.service.impl;

import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;
import com.costcook.service.UserService;
import com.costcook.util.OAuth2Properties;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final OAuth2Properties oAuth2Properties;

	/**
	 * OAuth provider에서 code를 통해 access token을 가져오고, 그 access token으로 사용자 정보를 추출한 후 OAuthUserInfo 객체로 반환한다.
	 * @param code - OAuth provider에서 전달된 인증 코드
	 * @param platformType - OAuth 플랫폼 유형 (Kakao, Google 등)
	 * @return OAuthUserInfo - 사용자 정보 (key, email, name, platformType)
	 */
	@Override
	public OAuthUserInfo getOAuthUserInfo(String code, PlatformTypeEnum platformType) {
		// 1. code를 통해 provider에서 제공하는 accessToken 가져온다.
		String accessTokenFromProvider = fetchAccessTokenFromProvider(code, platformType);
		log.info("provider 에서 제공하는 accessToken 값: {}", accessTokenFromProvider);
		
		// 2. provider에서 제공하는 accessToken으로 oAuthUserInfo를 추출한다.
		JsonNode oAuthUserNode = generateOAuthUserNode(accessTokenFromProvider, platformType);
		OAuthUserInfo oAuthUserInfo = fetchOAuthUserInfo(oAuthUserNode, platformType);
		return oAuthUserInfo;
	}

	/**
	 * 인증 코드를 통해 OAuth provider에서 access token을 가져온다.
	 * @param code - 인증 코드
	 * @param platformType - OAuth 플랫폼 유형
	 * @return accessToken - OAuth provider에서 제공한 액세스 토큰
	 */
	private String fetchAccessTokenFromProvider(String code, PlatformTypeEnum platformType) {
		// 설정 가져오기
		OAuth2Properties.Client client = getClientProperties(platformType);

		// 인증코드 디코딩
		String decodedCode = decodeCode(code);

		// HTTP 요청 헤더와 파라미터 생성
		HttpHeaders headers = createHeaders(client);
		MultiValueMap<String, String> params = createTokenRequestParams(client, decodedCode);
		
		// 토큰 요청 보내기
		ResponseEntity<Map> responseEntity = sendTokenRequest(client.getTokenUri(), headers, params);
		
		// 응답 검증 및 토큰 반환
		return extractAccessToken(responseEntity);
	}
	
	/**
	 * OAuth 플랫폼 유형에 따른 클라이언트 설정을 가져온다.
	 * @param platformType - OAuth 플랫폼 유형
	 * @return client - 해당 플랫폼의 클라이언트 설정
	 */
	private OAuth2Properties.Client getClientProperties(PlatformTypeEnum platformType) {
		OAuth2Properties.Client client = oAuth2Properties.getClients().get(platformType.getAuth());
		logClientDetails(client);
		return client;
	}
	
	/**
	 * 클라이언트 설정 정보를 로깅한다.
	 * @param client - OAuth 클라이언트 설정
	 */
	private void logClientDetails(OAuth2Properties.Client client) {
		log.info(client.getClientId());
		log.info(client.getClientSecret());
		log.info(client.getRedirectUri());
	}
	
	/**
	 * 인증 코드를 UTF-8로 디코딩한다.
	 * @param code - 디코딩할 인증 코드
	 * @return 디코딩된 코드
	 */
	private String decodeCode(String code) {
	    return URLDecoder.decode(code, StandardCharsets.UTF_8);
	}
	
	/**
	 * 클라이언트 ID와 클라이언트 시크릿을 바탕으로 인증 헤더를 생성한다.
	 * @param client - OAuth 클라이언트 설정
	 * @return 생성된 HTTP 헤더
	 */
	private HttpHeaders createHeaders(OAuth2Properties.Client client) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.setBasicAuth(client.getClientId(), client.getClientSecret());
	    return headers;
	}
	
	/**
	 * 액세스 토큰을 요청할 때 사용할 파라미터를 생성한다.
	 * @param client - OAuth 클라이언트 설정
	 * @param decodedCode - 디코딩된 인증 코드
	 * @return 생성된 파라미터 맵
	 */
	private MultiValueMap<String, String> createTokenRequestParams(OAuth2Properties.Client client, String decodedCode) {
		log.info("{}", client);
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("client_id", client.getClientId());
	    params.add("client_secret", client.getClientSecret());
	    params.add("code", decodedCode);
	    params.add("grant_type", "authorization_code");
	    params.add("redirect_uri", client.getRedirectUri());
	    return params;
	}
	
	/**
	 * 토큰 URI로 액세스 토큰을 요청한다.
	 * @param tokenUri - 토큰 요청 URI
	 * @param headers - HTTP 요청 헤더
	 * @param params - HTTP 요청 파라미터
	 * @return responseEntity - 토큰 요청에 대한 응답
	 */
	private ResponseEntity<Map> sendTokenRequest(String tokenUri, HttpHeaders headers, MultiValueMap<String, String> params) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
	    return restTemplate.postForEntity(tokenUri, requestEntity, Map.class);
	}
	
	/**
	 * 응답 엔티티에서 액세스 토큰을 추출한다.
	 * @param responseEntity - 액세스 토큰 요청 응답
	 * @return accessToken - 응답에서 추출한 액세스 토큰
	 */
	private String extractAccessToken(ResponseEntity<Map> responseEntity) {
	    if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 가져올 수 없음");
	    }
	    return (String) responseEntity.getBody().get("access_token");
	}
	
	/**
	 * OAuth provider에서 사용자 정보를 가져온다.
	 * @param accessToken - OAuth 액세스 토큰
	 * @param platformType - OAuth 플랫폼 유형
	 * @return 사용자 정보를 담고 있는 JsonNode 객체
	 */
	private JsonNode generateOAuthUserNode(String accessToken, PlatformTypeEnum platformType) {
		// 설정 가져오기
		OAuth2Properties.Client client = getClientProperties(platformType);
		
		// HTTP 요청 헤더 생성
		HttpHeaders headers = createAuthorizationHeaders(accessToken);
		
		// 사용자 정보 요청
		ResponseEntity<JsonNode> responseEntity = fetchUserInfoFromProvider(client.getUserInfoRequestUri(), headers);
		
		// 응답 검증 및 사용자 정보 반환
		return extractUserInfo(responseEntity);
	}
	
	/**
	 * 액세스 토큰을 사용하여 사용자 정보를 요청할 때 사용하는 헤더를 생성한다.
	 * @param accessToken - OAuth 액세스 토큰
	 * @return 생성된 HTTP 요청 헤더
	 */
	private HttpHeaders createAuthorizationHeaders(String accessToken) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + accessToken);
	    return headers;
	}
	
	/**
	 * 사용자 정보 URI로 사용자 정보를 요청한다.
	 * @param userInfoUri - 사용자 정보 요청 URI
	 * @param headers - HTTP 요청 헤더
	 * @return 사용자 정보에 대한 응답
	 */
	private ResponseEntity<JsonNode> fetchUserInfoFromProvider(String userInfoUri, HttpHeaders headers) {
	    RestTemplate restTemplate = new RestTemplate();
	    return restTemplate.exchange(userInfoUri, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);
	}
	
	/**
	 * 응답 엔티티에서 사용자 정보를 추출한다.
	 * @param responseEntity - 사용자 정보 요청 응답
	 * @return 사용자 정보를 담은 JsonNode 객체
	 */
	private JsonNode extractUserInfo(ResponseEntity<JsonNode> responseEntity) {
	    if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 가져올 수 없음");
	    }
	    return responseEntity.getBody();
	}
	
	/**
	 * OAuth provider에 따라 사용자 정보를 추출하여 OAuthUserInfo 객체를 생성한다.
	 * @param oAuthUserNode - 사용자 정보를 담고 있는 JsonNode 객체
	 * @param provider - OAuth 플랫폼 유형 (Kakao, Google 등)
	 * @return OAuthUserInfo - 사용자 정보 객체
	 */
	private OAuthUserInfo fetchOAuthUserInfo(JsonNode oAuthUserNode, PlatformTypeEnum provider) {
	    switch (provider) {
	        case KAKAO:
	            return extractKakaoUserInfo(oAuthUserNode);
	        case GOOGLE:
	            return extractGoogleUserInfo(oAuthUserNode);
	        default:
	            throw new IllegalArgumentException("지원하지 않는 플랫폼 유형입니다: " + provider);
	    }
	}

	/**
	 * Kakao 플랫폼으로부터 사용자 정보를 추출한다.
	 * @param oAuthUserNode - Kakao에서 제공한 사용자 정보가 담긴 JsonNode 객체
	 * @return OAuthUserInfo - Kakao 사용자 정보 객체
	 */
	private OAuthUserInfo extractKakaoUserInfo(JsonNode oAuthUserNode) {
	    String socialKey = oAuthUserNode.get("id").asText();
	    String name = extractJsonNodeText(oAuthUserNode, "properties", "nickname");
	    log.info("Kakao User Info - socialKey: {}, name: {}", socialKey, name);
	    return new OAuthUserInfo(socialKey, null, name, PlatformTypeEnum.KAKAO);
	}

	/**
	 * Google 플랫폼으로부터 사용자 정보를 추출한다.
	 * @param oAuthUserNode - Google에서 제공한 사용자 정보가 담긴 JsonNode 객체
	 * @return OAuthUserInfo - Google 사용자 정보 객체
	 */
	private OAuthUserInfo extractGoogleUserInfo(JsonNode oAuthUserNode) {
	    String socialKey = oAuthUserNode.get("sub").asText();
	    String email = oAuthUserNode.get("email").asText();
	    String name = oAuthUserNode.get("name").asText();
	    log.info("Google User Info - socialKey: {}, email: {}, name: {}", socialKey, email, name);
	    return new OAuthUserInfo(socialKey, email, name, PlatformTypeEnum.GOOGLE);
	}

	/**
	 * 주어진 부모 노드와 자식 노드 이름으로 JsonNode에서 텍스트 값을 추출한다.
	 * @param parentNode - 부모 JsonNode 객체
	 * @param childNodeName - 자식 노드 이름
	 * @return 추출된 텍스트 값
	 */
	private String extractJsonNodeText(JsonNode parentNode, String... childNodeName) {
	    JsonNode currentNode = parentNode;
	    for (String nodeName : childNodeName) {
	        if (currentNode == null || !currentNode.has(nodeName)) {
	            throw new IllegalArgumentException("해당 노드를 찾을 수 없습니다: " + nodeName);
	        }
	        currentNode = currentNode.get(nodeName);
	    }
	    return currentNode.asText();
	}
}
