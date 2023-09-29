package edu.sdccd.cisc191.client.controllers;

//import edu.sdccd.cisc191.common.entities.Stock;

import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StockController {
    /*@GetMapping("/stocks")
    public String showStocks() {
        return "stocks";
    }
    @PostMapping("/stocks")
    public String addStock(@Validated Stock stock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "stocks";
        }

        // TODO: call server
        return "redirect:/stocks";
    }*/

    @GetMapping("/stocks")
    public static String stocks() {
        return "stocks";
    }

    //CREATE, READ, UPDATE, DELETE
    public static void CreateFile(String filename, String content){
        // create a file with name [filename] and content [content]
    }

    public static String ReadFile(String filename){
        String contents = "The contents of the file";
        return contents;
    }

    public static void UpdateFile(String filename, String content){
        // access the file, then set its contents to [content]
    }

    public static void DeleteFile(String filename){
        //delete the file
    }
}
