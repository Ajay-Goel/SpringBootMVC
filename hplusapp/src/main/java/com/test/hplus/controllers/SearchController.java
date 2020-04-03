package com.test.hplus.controllers;

import com.test.hplus.beans.Product;
import com.test.hplus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Controller
public class SearchController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AsyncTaskExecutor executor;

//    @GetMapping("/search")
//    public Callable<String> search(@RequestParam("search") String search, Model model, HttpServletRequest request){
//        System.out.println("in search controller");
//        System.out.println("search criteria: "+search);
//        System.out.println("Async supported in Application: "+ request.isAsyncSupported());
//        System.out.println("Thread from the servlet container: "+Thread.currentThread().getName());
//        return ()->{
//            Thread.sleep(3000);
//            System.out.println("Thread from the spring mvc task executor: "+Thread.currentThread().getName());
//            List<Product> products = new ArrayList<>();
//            products = productRepository.searchByName(search);
//            model.addAttribute("products", products);
//            return "search";
//        };
//    }

    @GetMapping("/search")
    public DeferredResult<String> search(@RequestParam("search") String search, Model model, HttpServletRequest request){
        DeferredResult<String> deferredResult = new DeferredResult<>();
        // in deferred result we have the choice of managing the threads for it but in callable threads were working asyncronously.
        // when we use deferred then we have to manage the threads
        System.out.println("in search controller");
        System.out.println("search criteria: "+search);
        System.out.println("Async supported in Application: "+ request.isAsyncSupported());
        System.out.println("Thread from the servlet container: "+Thread.currentThread().getName());

        executor.execute(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread from the spring mvc task executor: "+Thread.currentThread().getName());
            List<Product> products = new ArrayList<>();
            products = productRepository.searchByName(search);
            model.addAttribute("products", products);
            //return "search";
            deferredResult.setResult("search");
        });
        return deferredResult;
    }



}
