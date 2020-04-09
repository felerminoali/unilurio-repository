package mz.ac.unilurio.repository.controller;

import mz.ac.unilurio.repository.model.Document;
import mz.ac.unilurio.repository.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Nullable;
import java.io.IOException;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private DocumentRepository repository;


    @GetMapping(value = "/")
    public String showPage() throws Exception {
        return "home.html";
    }

    @GetMapping(value = "/years/{year}/{category}/{search}", produces = "application/json")
    public @ResponseBody List<Document> showYears(@Nullable @PathVariable(name="year") Optional<String>  year, @Nullable @PathVariable(name="category") Optional<String>  category, @Nullable @PathVariable(name="search") Optional<String>  search)throws IOException {
        String tempYear = year.get().equals("null") ? null : year.get();
        String tempCategory = category.get().equals("null") ? null : category.get();
        String tempSearch = search.get().equals("null") ? null : search.get();

        List<Document> response = repository.findByFilter(tempYear, tempCategory, "GROUP BY d.year", tempSearch);
        return  response;
    }

    @GetMapping(value = "/categories/{year}/{category}/{search}", produces = "application/json")
    public @ResponseBody List<Document> showCategories(@Nullable @PathVariable(name="year") Optional<String>  year, @Nullable @PathVariable(name="category") Optional<String>  category, @Nullable @PathVariable(name="search") Optional<String>  search)throws IOException {
        String tempYear = year.get().equals("null") ? null : year.get();
        String tempCategory = category.get().equals("null") ? null : category.get();
        String tempSearch = search.get().equals("null") ? null : search.get();

        List<Document> response = repository.findByFilter(tempYear,tempCategory, "GROUP BY d.category",tempSearch);
        return  response;
    }

    @GetMapping(value = "/listdbfiles/{year}/{category}/{search}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Object> listDbFiles(@Nullable @PathVariable(name="year") Optional<String>  year, @Nullable @PathVariable(name="category") Optional<String>  category, @Nullable @PathVariable(name="search") Optional<String>  search)throws IOException {
        String tempYear = year.get().equals("null") ? null : year.get();
        String tempCategory = category.get().equals("null") ? null : category.get();
        String tempSearch = search.get().equals("null") ? null : search.get();

        Iterable<Document> documents = repository.findByFilter(tempYear, tempCategory, null,tempSearch);

        List<FileDisplay> list = new ArrayList<>();

        documents.forEach(document -> {

            FileDisplay display = new FileDisplay();
            display.setId(document.getGoogleId());

            String title = "<a href=\""+document.getUrl()+"\" target=\"_blank\">"+document.getTitle()+"</a>";

            display.setTitle(title);


            display.setYear(document.getYear());
            display.setUrl(document.getUrl());

//            String html = "<a href=\"#\" rel=\"" + document.getGoogleId()+ "\" class=\"view btn btn-default\" title=\"View\" data-toggle=\"tooltip\">Ver</a>";
            String html ="";
            html += "&nbsp;&nbsp;<a href=\"#\" rel=\"" + document.getGoogleId() + "\" class=\"edit btn btn-default\" title=\"Edit\" data-toggle=\"tooltip\">Editar</a>";
            html += "&nbsp;&nbsp;<a href=\"#\" rel=\"" + document.getGoogleId()+ "\" class=\"delete btn btn-default\" title=\"Delete\" data-toggle=\"tooltip\">Remover</a>";

            String attach = "";
//            for (Attachment attachment:document.getAttachmentCollection()) {
////                attach+="<div><a href=\""+attachment.getUrl()+"\" target=\"_blank\">"+attachment.getTitle()+"</a></div>";
////            }

            display.setAttachments(attach);
            display.setAction(html);
            list.add(display);

        });


        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data",  list);
        result.put("recordsTotal", list.size());
        result.put("draw", 1);
        result.put("recordsFiltered", list.size());

        return new ResponseEntity<Object>(result, HttpStatus.OK);

//        return  documents;
    }


}
