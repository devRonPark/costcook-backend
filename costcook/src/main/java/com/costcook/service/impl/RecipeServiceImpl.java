package com.costcook.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.domain.response.RecipeResponse;
import com.costcook.entity.Category;
import com.costcook.entity.RecipeIngredient;
import com.costcook.entity.RecipeItem;
import com.costcook.repository.CategoryRepository;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
	
	private final RecipeRepository recipeRepository;
	private RecipeIngredientRepository recipeIngredientRepository;

	
	// 레시피 목록 조회
	@Override
	public List<RecipeResponse> getRecipes(int page, int size, String sort, String order) {
		Pageable pageable = PageRequest.of(page, size);
		Page<RecipeItem> recipePage;
		
		// 정렬
        if (sort.equals("viewCount")) {
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByViewCountAsc(pageable);
        	} else { // 내림차순
        		recipePage = recipeRepository.findAllByOrderByViewCountDesc(pageable);
        	}
        } else if (sort.equals("avgRatings")) {
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByAvgRatingsAsc(pageable);
        	} else { // 내림차순
        		recipePage = recipeRepository.findAllByOrderByAvgRatingsDesc(pageable);        		
        	}
        } else { // 생성일(디폴트)
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByCreatedAtAsc(pageable);
        	} else {
        		recipePage = recipeRepository.findAllByOrderByCreatedAtDesc(pageable);        		
        	}
        }
		return recipePage.getContent().stream().map(RecipeResponse::toDTO).toList();     
	}
	
	
	// 전체 레시피 수 조회 : 총 페이지를 미리 입력하여, 무한 로딩 방지
	@Override
	public long getTotalRecipes() {
		return recipeRepository.count();
	}

	
	// 레시피 상세 조회
	@Override
	public RecipeResponse getRecipeById(Long id) {
		RecipeItem recipe = recipeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("레시피 정보가 없습니다."));
		RecipeResponse recipeResponse = RecipeResponse.toDTO(recipe);
		return recipeResponse;
	}
	
	
	// 재료 상세 조회
	

	

	
	
	
	
	
	
	
	
	
	// 테이블끼리 연결된 객체 타입의 컬럼들을 findById로 Long Id만 가져오는 메소드
	private final CategoryRepository categoryRepository;
	
	public Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
	
	// DB에 직접 데이터를 넣는 메소드
	@Override
	@Transactional
	public void insertRecipes() {

        RecipeItem[] recipes = {
        		new RecipeItem(1L, findCategoryById(15L), 772107, "감자멸치고추장찌개", "772107", "설명1", 3, 2323, null, null, 53, 19, 52, 4.9),
        		new RecipeItem(2L, findCategoryById(16L), 760950, "감자볶음파스타", "760950", "설명2", 1, 960, null, null, 59, 28, 35, 0.0),
        		new RecipeItem(3L, findCategoryById(17L), 763499, "단호박찹쌀도넛", "763499", "설명3", 3, 4604, null, null, 69, 16, 57, 0.9),
        		new RecipeItem(4L, findCategoryById(18L), 746946, "닭고기완자", "746946", "설명4", 4, 4706, null, null, 81, 1, 2, 1.5),
        		new RecipeItem(5L, findCategoryById(17L), 747797, "딸기쨈머핀", "747797", "설명5", 4, 7942, null, null, 15, 9, 76, 3.8),
        		new RecipeItem(6L, findCategoryById(19L), 769867, "문어꽈리고추조림", "769867", "설명6", 4, 12853, null, null, 45, 4, 36, 4.6),
        		new RecipeItem(7L, findCategoryById(19L), 775725, "버섯두부구이", "775725", "설명7", 2, 6041, null, null, 19, 24, 51, 0.9),
        		new RecipeItem(8L, findCategoryById(17L), 767035, "스위트콘피자토스트", "767035", "설명8", 2, 1571, null, null, 16, 11, 78, 1.0),
        		new RecipeItem(9L, findCategoryById(17L), 760898, "연유바게트", "760898", "설명9", 3, 1659, null, null, 8, 4, 35, 3.2),
        		new RecipeItem(10L, findCategoryById(19L), 746887, "찬밥동그랑땡", "746887", "설명10", 2, 3307, null, null, 84, 2, 25, 3.1),
        		new RecipeItem(11L, findCategoryById(20L), 775819, "참치카레스파게티", "775819", "설명11", 2, 3735, null, null, 56, 27, 81, 2.9),
        		new RecipeItem(12L, findCategoryById(19L), 765584, "파프리카햄전", "765584", "설명12", 2, 3188, null, null, 80, 0, 71, 3.4),
        		new RecipeItem(13L, findCategoryById(19L), 128892, "두부새우전", "128892", "설명13", 3, 7876, null, null, 99, 26, 36, 2.8),
        		new RecipeItem(14L, findCategoryById(21L), 131871, "현미호두죽", "131871", "설명14", 2, 4645, null, null, 7, 4, 8, 2.3),
        		new RecipeItem(15L, findCategoryById(20L), 149207, "토마토스파게티", "149207", "설명15", 1, 3425, null, null, 38, 4, 56, 4.8),
        		new RecipeItem(16L, findCategoryById(21L), 221097, "참치삼각김밥", "221097", "설명16", 2, 1800, null, null, 44, 20, 40, 3.9),
        		new RecipeItem(17L, findCategoryById(17L), 221101, "스팸샌드위치", "221101", "설명17", 1, 1357, null, null, 13, 20, 15, 0.4),
        		new RecipeItem(18L, findCategoryById(21L), 221102, "참치깻잎주먹밥", "221102", "설명18", 2, 6092, null, null, 49, 11, 9, 1.5),
        		new RecipeItem(19L, findCategoryById(17L), 223583, "새우볶음밥고로케", "223583", "설명19", 2, 15718, null, null, 25, 6, 94, 2.7),
        		new RecipeItem(20L, findCategoryById(21L), 223584, "스팸쌈밥", "223584", "설명20", 2, 1719, null, null, 82, 6, 57, 2.9),
        		new RecipeItem(21L, findCategoryById(17L), 225146, "스팸오이식빵롤", "225146", "설명21", 2, 915, null, null, 73, 2, 37, 1.3),
        		new RecipeItem(22L, findCategoryById(17L), 340537, "미니새우버거", "340537", "설명22", 3, 11896, null, null, 6, 9, 4, 3.8),
        		new RecipeItem(23L, findCategoryById(20L), 346665, "뚝배기라면", "346665", "설명23", 1, 5394, null, null, 87, 21, 50, 2.9),
        		new RecipeItem(24L, findCategoryById(20L), 349332, "야끼만두", "349332", "설명24", 2, 2834, null, null, 52, 14, 56, 3.4),
        		new RecipeItem(25L, findCategoryById(20L), 374689, "소고기크림치즈오븐스파게티", "374689", "설명25", 1, 2911, null, null, 34, 0, 78, 1.5),
        		new RecipeItem(26L, findCategoryById(17L), 380934, "바나나프렌치토스트", "380934", "설명26", 1, 1905, null, null, 76, 18, 13, 2.2),
        		new RecipeItem(27L, findCategoryById(20L), 383305, "파스타", "383305", "설명27", 1, 1303, null, null, 82, 6, 52, 4.6),
        		new RecipeItem(28L, findCategoryById(17L), 384505, "흑미와플", "384505", "설명28", 2, 4467, null, null, 99, 28, 35, 1.2),
        		new RecipeItem(29L, findCategoryById(17L), 384888, "튜나에그샌드위치", "384888", "설명29", 2, 3276, null, null, 43, 3, 58, 1.2),
        		new RecipeItem(30L, findCategoryById(21L), 387126, "사천짜장떡볶이", "387126", "설명30", 3, 6000, null, null, 8, 13, 60, 2.9),
        		new RecipeItem(31L, findCategoryById(21L), 414203, "전자렌지딸기찹쌀떡", "414203", "설명31", 1, 4386, null, null, 95, 29, 8, 3.7),
        		new RecipeItem(32L, findCategoryById(17L), 414255, "닭고기샌드위치", "414255", "설명32", 1, 5838, null, null, 84, 3, 8, 0.6),
        		new RecipeItem(33L, findCategoryById(21L), 414544, "김치볶음밥", "414544", "설명33", 1, 13709, null, null, 65, 8, 45, 2.4),
        		new RecipeItem(34L, findCategoryById(22L), 420422, "토마토카프레제샐러드", "420422", "설명34", 2, 4084, null, null, 12, 6, 14, 2.3),
        		new RecipeItem(35L, findCategoryById(19L), 420425, "오징어청양고추조림", "420425", "설명35", 4, 9097, null, null, 31, 7, 24, 0.3),
        		new RecipeItem(36L, findCategoryById(21L), 421940, "샐러드오므라이스", "421940", "설명36", 3, 17311, null, null, 21, 12, 55, 3.4),
        		new RecipeItem(37L, findCategoryById(17L), 507405, "브런치", "507405", "설명37", 1, 808, null, null, 53, 18, 90, 2.0),
        		new RecipeItem(38L, findCategoryById(21L), 513745, "밥샌드위치", "513745", "설명38", 1, 6753, null, null, 30, 3, 20, 2.7),
        		new RecipeItem(39L, findCategoryById(19L), 517865, "연근전", "517865", "설명39", 2, 6338, null, null, 43, 3, 98, 4.1),
        		new RecipeItem(40L, findCategoryById(17L), 519491, "홈런볼", "519491", "설명40", 4, 2224, null, null, 79, 24, 7, 3.0),
        		new RecipeItem(41L, findCategoryById(20L), 519510, "해물볶음라면", "519510", "설명41", 1, 9141, null, null, 74, 19, 51, 4.5),
        		new RecipeItem(42L, findCategoryById(15L), 527132, "꽁치김치찌개", "527132", "설명42", 2, 12331, null, null, 26, 5, 84, 4.0),
        		new RecipeItem(43L, findCategoryById(18L), 533182, "코다리양념구이", "533182", "설명43", 2, 5856, null, null, 90, 25, 43, 4.2),
        		new RecipeItem(44L, findCategoryById(23L), 536299, "부추된장국", "536299", "설명44", 2, 2240, null, null, 46, 18, 52, 1.0),
        		new RecipeItem(45L, findCategoryById(18L), 540257, "부추제육볶음", "540257", "설명45", 3, 21450, null, null, 48, 29, 62, 4.6),
        		new RecipeItem(46L, findCategoryById(24L), 544697, "김치오믈렛", "544697", "설명46", 2, 15687, null, null, 91, 5, 6, 3.1),
        		new RecipeItem(47L, findCategoryById(21L), 554907, "쌈밥", "554907", "설명47", 1, 3900, null, null, 97, 5, 12, 4.7),
        		new RecipeItem(48L, findCategoryById(21L), 565100, "치킨까스카레덮밥", "565100", "설명48", 4, 7452, null, null, 19, 1, 14, 2.4),
        		new RecipeItem(49L, findCategoryById(19L), 567959, "달래김무침", "567959", "설명49", 2, 400, null, null, 50, 6, 35, 3.9),
        		new RecipeItem(50L, findCategoryById(17L), 574700, "견과류파운드케익", "574700", "설명50", 4, 5551, null, null, 5, 28, 63, 3.3),
        		new RecipeItem(51L, findCategoryById(18L), 576938, "돼지고기야채볶음", "576938", "설명51", 2, 11679, null, null, 27, 13, 9, 0.2),
        		new RecipeItem(52L, findCategoryById(17L), 577365, "쌀가루치즈케이크", "577365", "설명52", 4, 2664, null, null, 8, 5, 34, 1.7),
        		new RecipeItem(53L, findCategoryById(22L), 578044, "연어계란샐러드", "578044", "설명53", 3, 25939, null, null, 69, 11, 78, 0.3),
        		new RecipeItem(54L, findCategoryById(17L), 581188, "두부또띠아피자", "581188", "설명54", 4, 8800, null, null, 32, 13, 44, 1.8),
        		new RecipeItem(55L, findCategoryById(19L), 585127, "꼬막볶음", "585127", "설명55", 2, 6585, null, null, 24, 28, 89, 4.7),
        		new RecipeItem(56L, findCategoryById(22L), 587520, "딸기견과류샐러드", "587520", "설명56", 2, 8939, null, null, 83, 7, 34, 4.6),
        		new RecipeItem(57L, findCategoryById(24L), 593319, "두부샌드그라탕", "593319", "설명57", 2, 7663, null, null, 57, 28, 17, 0.6),
        		new RecipeItem(58L, findCategoryById(21L), 593560, "브로콜리새우볶음밥", "593560", "설명58", 2, 11873, null, null, 29, 7, 52, 4.3),
        		new RecipeItem(59L, findCategoryById(18L), 617567, "풋마늘오징어무침", "617567", "설명59", 2, 27581, null, null, 7, 25, 28, 5.0),
        		new RecipeItem(60L, findCategoryById(21L), 617967, "칠리참치롤", "617967", "설명60", 1, 372, null, null, 82, 22, 31, 0.2),
        		new RecipeItem(61L, findCategoryById(22L), 618174, "쉬림프딸기샐러드", "618174", "설명61", 2, 11546, null, null, 96, 18, 16, 1.0),
        		new RecipeItem(62L, findCategoryById(17L), 623625, "딸기바나나크레이프", "623625", "설명62", 3, 5164, null, null, 49, 29, 76, 1.9),
        		new RecipeItem(63L, findCategoryById(24L), 654934, "감자계란그라탕", "654934", "설명63", 2, 2313, null, null, 4, 4, 33, 0.1),
        		new RecipeItem(64L, findCategoryById(23L), 708450, "감자전", "708450", "설명64", 1, 1845, null, null, 94, 22, 12, 4.9),
        		new RecipeItem(65L, findCategoryById(23L), 723670, "감자주먹밥구이", "723670", "설명65", 1, 2613, null, null, 68, 27, 89, 1.8),
        		new RecipeItem(66L, findCategoryById(23L), 719033, "감자채카레볶음", "719033", "설명66", 4, 1469, null, null, 51, 18, 27, 2.2),
        		new RecipeItem(67L, findCategoryById(23L), 691033, "감자핫케익", "691033", "설명67", 3, 1674, null, null, 38, 23, 0, 0.8),
        		new RecipeItem(68L, findCategoryById(23L), 639616, "계라주먹밥", "639616", "설명68", 4, 5911, null, null, 35, 3, 34, 2.0),
        		new RecipeItem(69L, findCategoryById(23L), 713091, "고구마만주", "713091", "설명69", 3, 3203, null, null, 30, 6, 93, 0.1),
        		new RecipeItem(70L, findCategoryById(23L), 671377, "굴생채", "671377", "설명70", 2, 3549, null, null, 40, 5, 15, 2.3),
        		new RecipeItem(71L, findCategoryById(23L), 669312, "굴소스짜장볶음밥", "669312", "설명71", 2, 4389, null, null, 93, 5, 30, 0.0),
        		new RecipeItem(72L, findCategoryById(23L), 719519, "김치감자샐러드", "719519", "설명72", 1, 4155, null, null, 57, 9, 42, 1.3),
        		new RecipeItem(73L, findCategoryById(23L), 637848, "노오븐마늘빵", "637848", "설명73", 1, 862, null, null, 30, 25, 55, 2.9),
        		new RecipeItem(74L, findCategoryById(23L), 625080, "녹차롤케이크", "625080", "설명74", 4, 5767, null, null, 74, 2, 93, 0.8),
        		new RecipeItem(75L, findCategoryById(23L), 693666, "달걀볶음밥", "693666", "설명75", 1, 1544, null, null, 11, 19, 34, 2.6),
        		new RecipeItem(76L, findCategoryById(23L), 710794, "닭안심짜장덮밥", "710794", "설명76", 2, 7762, null, null, 92, 16, 34, 4.0),
        		new RecipeItem(77L, findCategoryById(24L), 652830, "돼지목살김치찌개", "652830", "설명77", 3, 7831, null, null, 46, 2, 53, 2.1),
        		new RecipeItem(78L, findCategoryById(23L), 695395, "두부강정", "695395", "설명78", 1, 4973, null, null, 0, 2, 32, 3.7),
        		new RecipeItem(79L, findCategoryById(23L), 671138, "뚝배기달걀찜", "671138", "설명79", 2, 1893, null, null, 28, 12, 84, 0.9),
        		new RecipeItem(80L, findCategoryById(23L), 681921, "매콤짜장덮밥", "681921", "설명80", 4, 5423, null, null, 6, 16, 6, 2.9),
        		new RecipeItem(81L, findCategoryById(23L), 663499, "묵은지유부초밥", "663499", "설명81", 2, 3947, null, null, 61, 15, 91, 0.1),
        		new RecipeItem(82L, findCategoryById(24L), 729403, "미니스카치에그", "729403", "설명82", 3, 3469, null, null, 43, 2, 76, 3.6),
        		new RecipeItem(83L, findCategoryById(18L), 665517, "미트볼야채조림", "665517", "설명83", 4, 16648, null, null, 41, 10, 68, 3.8),
        		new RecipeItem(84L, findCategoryById(23L), 713916, "바나나통밀팬케이크", "713916", "설명84", 3, 8834, null, null, 11, 2, 52, 2.1),
        		new RecipeItem(85L, findCategoryById(23L), 724999, "브로콜리계란샌드위치", "724999", "설명85", 3, 14620, null, null, 81, 14, 87, 3.8),
        		new RecipeItem(86L, findCategoryById(23L), 685161, "비엔나마늘종볶음", "685161", "설명86", 2, 1692, null, null, 38, 2, 82, 4.9),
        		new RecipeItem(87L, findCategoryById(24L), 678008, "새송이버섯라이스그라탕", "678008", "설명87", 3, 9703, null, null, 39, 18, 16, 4.4),
        		new RecipeItem(88L, findCategoryById(23L), 712064, "샌드위치", "712064", "설명88", 1, 1125, null, null, 60, 23, 66, 3.3),
        		new RecipeItem(89L, findCategoryById(23L), 672563, "소고기콩나물두부국", "672563", "설명89", 3, 6698, null, null, 23, 18, 61, 1.9),
        		new RecipeItem(90L, findCategoryById(24L), 695402, "순두부찌개", "695402", "설명90", 1, 2661, null, null, 81, 21, 96, 2.9),
        		new RecipeItem(91L, findCategoryById(23L), 729379, "스팸두부전골", "729379", "설명91", 4, 4983, null, null, 3, 5, 56, 3.1),
        		new RecipeItem(92L, findCategoryById(24L), 714140, "스팸볶음밥그라탕", "714140", "설명92", 2, 8063, null, null, 56, 28, 48, 1.2),
        		new RecipeItem(93L, findCategoryById(21L), 683113, "쑥떡떡볶이", "683113", "설명93", 3, 8519, null, null, 26, 22, 14, 2.3),
        		new RecipeItem(94L, findCategoryById(24L), 702096, "애호박돼지고기찌개", "702096", "설명94", 2, 4802, null, null, 39, 25, 67, 2.4),
        		new RecipeItem(95L, findCategoryById(23L), 629687, "양상추샌드위치", "629687", "설명95", 1, 17477, null, null, 2, 11, 16, 0.5),
        		new RecipeItem(96L, findCategoryById(19L), 672814, "양송이버섯장조림", "672814", "설명96", 4, 14435, null, null, 82, 20, 5, 0.1),
        		new RecipeItem(97L, findCategoryById(23L), 710810, "어묵샌드크로켓", "710810", "설명97", 4, 4370, null, null, 57, 24, 79, 3.8),
        		new RecipeItem(98L, findCategoryById(23L), 666820, "오렌지갈레트", "666820", "설명98", 4, 4193, null, null, 47, 14, 53, 5.0),
        		new RecipeItem(99L, findCategoryById(21L), 685904, "오므라이스", "685904", "설명99", 1, 1027, null, null, 81, 6, 51, 0.7),
        		new RecipeItem(100L, findCategoryById(23L), 647191, "오믈렛버거", "647191", "설명100", 3, 8634, null, null, 75, 27, 66, 2.8),
        		new RecipeItem(101L, findCategoryById(21L), 668311, "오징어토마토덮밥", "668311", "설명101", 1, 6610, null, null, 39, 28, 88, 4.0),
        		new RecipeItem(102L, findCategoryById(21L), 697642, "전자렌지찹쌀떡", "697642", "설명102", 2, 932, null, null, 96, 25, 46, 1.0),
        		new RecipeItem(103L, findCategoryById(19L), 712015, "참치볼조림", "712015", "설명103", 1, 1604, null, null, 19, 5, 56, 0.0),
        		new RecipeItem(104L, findCategoryById(18L), 712007, "치킨데리야끼", "712007", "설명104", 1, 3846, null, null, 47, 26, 29, 3.1),
        		new RecipeItem(105L, findCategoryById(23L), 683843, "치킨시저샐러드", "683843", "설명105", 3, 12774, null, null, 51, 28, 38, 1.3),
        		new RecipeItem(106L, findCategoryById(20L), 675474, "치킨카레우동", "675474", "설명106", 1, 14337, null, null, 22, 26, 40, 4.6),
        		new RecipeItem(107L, findCategoryById(23L), 690873, "카레두부햄버거", "690873", "설명107", 3, 13304, null, null, 61, 24, 85, 1.0),
        		new RecipeItem(108L, findCategoryById(19L), 731160, "카레실파전", "731160", "설명108", 2, 1749, null, null, 70, 21, 66, 3.3),
        		new RecipeItem(109L, findCategoryById(23L), 647188, "카프레제크로와상샌드위치", "647188", "설명109", 3, 5447, null, null, 36, 1, 45, 4.9),
        		new RecipeItem(110L, findCategoryById(23L), 671399, "크랜베리버터케이크", "671399", "설명110", 4, 10945, null, null, 13, 2, 93, 2.9),
        		new RecipeItem(111L, findCategoryById(23L), 725727, "크림참치베이글샌드위치", "725727", "설명111", 1, 722, null, null, 60, 19, 92, 0.9),
        		new RecipeItem(112L, findCategoryById(23L), 658148, "티라미스", "658148", "설명112", 4, 4679, null, null, 6, 2, 49, 2.8),
        		new RecipeItem(113L, findCategoryById(19L), 728438, "파프리카전", "728438", "설명113", 2, 3909, null, null, 75, 26, 32, 2.4),
        		new RecipeItem(114L, findCategoryById(20L), 704187, "표고버섯해물스파게티", "704187", "설명114", 2, 39736, null, null, 83, 9, 88, 3.7),
        		new RecipeItem(115L, findCategoryById(24L), 688113, "푸실리", "688113", "설명115", 2, 5875, null, null, 80, 5, 32, 2.0),
        		new RecipeItem(116L, findCategoryById(23L), 707340, "플레인요구르트", "707340", "설명116", 4, 7450, null, null, 53, 22, 18, 0.8),
        		new RecipeItem(117L, findCategoryById(23L), 727989, "피자샌드", "727989", "설명117", 2, 1948, null, null, 60, 14, 72, 0.7),
        		new RecipeItem(118L, findCategoryById(24L), 730301, "해물된장찌개", "730301", "설명118", 2, 3104, null, null, 77, 22, 95, 4.6),
        		new RecipeItem(119L, findCategoryById(20L), 712864, "해물볶음우동", "712864", "설명119", 1, 11132, null, null, 87, 26, 13, 4.5),
        		new RecipeItem(120L, findCategoryById(21L), 677769, "해물콩나물밥", "677769", "설명120", 4, 26188, null, null, 2, 7, 8, 0.1),
        		new RecipeItem(121L, findCategoryById(19L), 704277, "호박볶음", "704277", "설명121", 1, 2993, null, null, 8, 9, 19, 1.3),
        		new RecipeItem(122L, findCategoryById(18L), 676978, "황태오븐구이", "676978", "설명122", 4, 26954, null, null, 67, 10, 63, 3.8),
        		new RecipeItem(123L, findCategoryById(23L), 685164, "흑미식빵", "685164", "설명123", 4, 4904, null, null, 15, 3, 2, 2.5),
        };
        // DB 저장
        for (RecipeItem recipe : recipes) {
            recipeRepository.save(recipe);
        }
    }


	

	

}
