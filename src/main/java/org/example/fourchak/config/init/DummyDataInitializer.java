//package org.example.fourchak.config.init;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import lombok.RequiredArgsConstructor;
//import org.example.fourchak.domain.store.entity.Store;
//import org.example.fourchak.domain.store.repository.StoreRepository;
//import org.example.fourchak.domain.user.entity.User;
//import org.example.fourchak.domain.user.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DummyDataInitializer implements CommandLineRunner {
//
//    private final StoreRepository storeRepository;
//    private final UserRepository userRepository;
//    private final Random random = new Random();
//
//    // 다양한 업종별 이름 템플릿
//    private final String[] restaurantPrefixes = {
//        "할머니", "우리집", "맛고을", "행복한", "정성", "황금", "진미", "별미", "옛날", "고향",
//        "왕가", "소문난", "유명한", "전통", "맛나", "깔끔한", "신선한", "건강한", "자연", "푸른"
//    };
//
//    private final String[] restaurantTypes = {
//        "한식당", "중국집", "일식집", "양식당", "분식집", "치킨집", "피자집", "족발집", "곱창집", "삼겹살집",
//        "냉면집", "국밥집", "찌개집", "떡볶이집", "김밥천국", "도시락", "카페", "베이커리", "아이스크림", "디저트카페"
//    };
//
//    private final String[] franchiseNames = {
//        "맥도날드", "KFC", "버거킹", "롯데리아", "맘스터치", "스타벅스", "투썸플레이스", "카페베네", "이디야커피",
//        "파리바게뜨", "뚜레주르", "던킨도너츠", "베스킨라빈스", "배달의민족", "요기요카페", "컴포즈커피",
//        "페리카나", "굽네치킨", "BBQ", "교촌치킨", "네네치킨", "처갓집양념치킨", "도미노피자", "피자헛", "미스터피자"
//    };
//
//    private final String[] uniqueNames = {
//        "달빛소나타", "바람의언덕", "하늘정원", "별빛마을", "꿈의정원", "행운의집", "희망카페", "사랑방",
//        "추억의거리", "낭만포차", "청춘다방", "그리움식당", "향수카페", "옛정", "인연", "만남의광장",
//        "소풍", "나들이", "휴식", "여유", "감성", "힐링스팟", "소소한행복", "따뜻한집", "포근한방"
//    };
//
//    private final String[] areas = {
//        "강남", "홍대", "명동", "신촌", "이태원", "건대", "노원", "송파", "마포", "성수",
//        "압구정", "청담", "역삼", "선릉", "삼성", "잠실", "방배", "서초", "용산", "종로"
//    };
//
//    @Override
//    public void run(String... args) {
//        if (storeRepository.count() >= 50000) {
//            return;  // 중복 생성 방지
//        }
//
//        // 더미 사용자 생성
//        User dummyUser = new User(
//            "dummy@user.com",
//            "더미사장",
//            "010-0000-0000",
//            "encodedpassword",
//            "OWNER"
//        );
//        userRepository.save(dummyUser);
//
//        List<Store> dummyStores = new ArrayList<>();
//
//        for (int i = 1; i <= 50000; i++) {
//            String storeName = generateDiverseStoreName(i);
//            String phoneNumber = generatePhoneNumber();
//            int capacity = generateCapacity();
//
//            Store store = new Store(
//                storeName,
//                phoneNumber,
//                capacity,
//                dummyUser
//            );
//            dummyStores.add(store);
//        }
//
//        // 대량 저장을 위한 배치 처리 (1000개씩 나눠 저장)
//        for (int i = 0; i < dummyStores.size(); i += 1000) {
//            int end = Math.min(i + 1000, dummyStores.size());
//            storeRepository.saveAll(dummyStores.subList(i, end));
//
//            // 진행 상황 출력
//            if ((i / 1000 + 1) % 10 == 0) {
//                System.out.println((i / 1000 + 1) * 1000 + "개 매장 생성 완료...");
//            }
//        }
//
//        System.out.println("총 50,000개의 다양한 더미 Store가 생성되었습니다.");
//    }
//
//    private String generateDiverseStoreName(int index) {
//        int nameType = index % 6; // 6가지 타입으로 분류
//
//        switch (nameType) {
//            case 0: // 전통적인 한식당 스타일
//                return restaurantPrefixes[random.nextInt(restaurantPrefixes.length)] +
//                    restaurantTypes[random.nextInt(restaurantTypes.length)];
//
//            case 1: // 프랜차이즈 + 지점명
//                return franchiseNames[random.nextInt(franchiseNames.length)] + " " +
//                    areas[random.nextInt(areas.length)] + "점";
//
//            case 2: // 감성적인 이름
//                return uniqueNames[random.nextInt(uniqueNames.length)];
//
//            case 3: // 지역명 + 업종
//                return areas[random.nextInt(areas.length)] + " " +
//                    restaurantTypes[random.nextInt(restaurantTypes.length)];
//
//            case 4: // 번호가 있는 체인점 스타일
//                return restaurantPrefixes[random.nextInt(restaurantPrefixes.length)] +
//                    restaurantTypes[random.nextInt(restaurantTypes.length)] +
//                    " " + (random.nextInt(20) + 1) + "호점";
//
//            default: // 개성있는 조합
//                return uniqueNames[random.nextInt(uniqueNames.length)] + " " +
//                    restaurantTypes[random.nextInt(restaurantTypes.length)];
//        }
//    }
//
//    private String generatePhoneNumber() {
//        // 서울 지역번호들을 랜덤하게 사용
//        String[] areaCodes = {"02", "031", "032", "051", "052", "053", "054", "055", "061", "062",
//            "063", "064"};
//        String areaCode = areaCodes[random.nextInt(areaCodes.length)];
//
//        int middle = 1000 + random.nextInt(9000); // 1000~9999
//        int last = 1000 + random.nextInt(9000);   // 1000~9999
//
//        return areaCode + "-" + middle + "-" + last;
//    }
//
//    private int generateCapacity() {
//        // 업종별로 다른 수용 인원 범위 설정
//        int[] capacityRanges = {20, 30, 50, 80, 100, 150, 200};
//        return capacityRanges[random.nextInt(capacityRanges.length)] + random.nextInt(20);
//    }
//}