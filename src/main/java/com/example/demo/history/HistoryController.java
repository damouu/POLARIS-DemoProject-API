package com.example.demo.history;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@Validated
@RestController
@RequestMapping("api/history")
public class HistoryController {

    private final HistoryService historyService;


}
