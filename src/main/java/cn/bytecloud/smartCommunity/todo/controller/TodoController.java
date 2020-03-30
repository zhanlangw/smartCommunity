package cn.bytecloud.smartCommunity.todo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.bytecloud.smartCommunity.todo.controller.TodoController.API;

@RestController
@RequestMapping(API)
public class TodoController {
    public static final String API = "/api/todo/";


}
