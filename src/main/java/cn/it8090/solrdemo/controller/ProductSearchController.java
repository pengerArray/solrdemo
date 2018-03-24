package cn.it8090.solrdemo.controller;



import cn.it8090.solrdemo.po.Products;
import cn.it8090.solrdemo.po.ProductsQuery;
import cn.it8090.solrdemo.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description
 * @PoweredBy Amour Mars
 * @Date 20:27 2018/1/23
 */
@Controller
public class ProductSearchController {

    @Autowired
    private ProductSearchService productSearchService;


    @RequestMapping("/search")
    public String search(Model model, ProductsQuery productsQuery) throws Exception {

        List<Products> products = productSearchService.searchProducts(productsQuery);

        model.addAttribute("list", products);

        return "search";
    }
}
