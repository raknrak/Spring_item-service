package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final이 붙은 멤버변수만 생성자를 자동으로 만들어줌
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) { // 아이템 목록 출력
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }
    @GetMapping("/{itemId}")
    //PathVarable로 넘어온 상품ID를 조회하고, 모델에 담아둠 -> 뷰 템플릿 호출
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item);// 자동 추가, 생략 가능, Model도 생략 가능
        return "basic/item";
        //@ModelAttribute - 요청 파라미터 처리
        //@ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
        //@ModelAttribute - Model 추가
        //@ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체
        //를 자동으로 넣어준다. 지금 코드를 보면 model.addAttribute("item", item) 가 주석처리 되어 있어도 잘 동
        //작하는 것을 확인할 수 있다.
        //모델에 데이터를 담을 때는 이름이 필요하다. 이름은 @ModelAttribute 에 지정한 name(value) 속성을 사용한
        //다. 만약 다음과 같이 @ModelAttribute 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.
        //@ModelAttribute("hello") Item item  이름을 hello 로 지정
        //model.addAttribute("hello", item);  모델에 hello 이름으로 저장
    }

    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // nameValue를 생략하면
        // Class명인 Item -> item ( 첫 글자를 소문자로 바꿔준다)
        itemRepository.save(item);
        return "basic/item";
        //@ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 등록한다.
        //예) @ModelAttribute 클래스명 모델에 자동 추가되는 이름
        //Item -> item
        //HelloWorld -> helloWorld
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
        //@ModelAttribute 자체도 생략가능하다. 대상 객체는 모델에 자동 등록된다
    }

    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
    // 테스트용 데이터 추가
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("testA", 1000, 10));
        itemRepository.save(new Item("testB", 1000, 10));
    }

}
