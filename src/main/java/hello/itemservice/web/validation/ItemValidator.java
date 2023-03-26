package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 검증 로직을 담당하는 객체
 */
@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        // 매개변수 Class 가 Item 과 같은 타입인지 확인하는 로직
        // isAssignableFrom 은 자식 Class 까지도 true 를 반환한다.
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 매개변수를 Item 으로 케스팅
        Item item = (Item) target;

        // Errors 는 BindingResult 의 부모 클레스 이므로
        // BindingResult 도 매개변수로 받을 수 있다.
        if (!StringUtils.hasText(item.getItemName()))
            errors.rejectValue("itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100000)
            errors.rejectValue("price", "range",
                    new Object[]{1000,1000000}, null);

        if (item.getQuantity() == null || item.getQuantity() >= 9999)
            errors.rejectValue("quantity", "max",
                    new Object[]{9999}, null);

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000)
                errors.reject("totalPriceMin",
                        new Object[]{10000, resultPrice}, null);
        }

    }
}
