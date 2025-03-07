package nakup.order.controller;

import nakup.order.dto.TestRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @GetMapping("/test")
    public List<TestRequest> test(@RequestBody TestRequest testRequest1, @RequestBody TestRequest testRequest2) {
        return new ArrayList<>();
    }
}
