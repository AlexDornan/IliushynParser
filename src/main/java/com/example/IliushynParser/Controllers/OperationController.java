package com.example.IliushynParser.Controllers;

import com.example.IliushynParser.Models.ProductData;
import com.example.IliushynParser.Services.TableGenerationService;
import com.example.IliushynParser.Services.ParserService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class OperationController {

    private final ParserService parserService;
    private final TableGenerationService tableGenerationService;

    @Autowired
    public OperationController(ParserService parserService, TableGenerationService tableGenerationService) {
        this.parserService = parserService;
        this.tableGenerationService = tableGenerationService;
    }

    @GetMapping("/")
    public String MainPage() {
        return "index";
    }

    @GetMapping("/index")
    public String MainPageIndex() {
        return "index";
    }

    @GetMapping("/parser")
    public String ParserPage(Model model) {
        model.addAttribute("infoText", "Wellcome to Rozetka parser.");
        return "parser";
    }

    @GetMapping("/about")
    public String AboutPage(Model model) {
        model.addAttribute("infoHeader", "Ілюшин О.С. 122-20ск");
        model.addAttribute("infoAboutMe", "Це моя лабораторна робота з дисципліни: Поглиблена Java. Розроблено парсер для сайту Rozetka.");
        return "about";
    }

    @PostMapping("/inject")
    public ResponseEntity<ByteArrayResource> Search(Model model, @RequestParam String name) throws InterruptedException, IOException, URISyntaxException {
        List<ProductData> adList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyy_hh_mm_ss_a");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        String fileName = "Search_result_on_Rosetka_" + date;
        if (name.isEmpty()){
            fileName = "Request_Was_Empty_" + date;
            ProductData emp = new ProductData();
            emp.SetSearch("Request_Was_Empty");
            emp.SetDescription("Request_Was_Empty");
            adList.add(emp);
        }
        else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            WebDriver driver = new ChromeDriver(options);
            ParserService pars = new ParserService();
            pars.EstablishConnection(driver);
            adList = pars.Parse(name);
            pars.DisconnectBrowserConnection(driver);
        }
        if (adList.size() != 0) {
            List<String> captionColumn = new ArrayList<>();
            captionColumn.add("Search request");
            captionColumn.add("Product number");
            captionColumn.add("Description");
            captionColumn.add("Price");
            captionColumn.add("Available");
            captionColumn.add("Link");
            tableGenerationService.CreateColumnCaptions(captionColumn);
            tableGenerationService.CreateExcel(fileName, adList);
        }
        String FILE_PATH = "./GeneratedFiles/" + fileName + ".xls";
        Path path = Paths.get(FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        Thread.sleep(1000);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
