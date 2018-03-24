package cn.it8090.solrdemo.service;


import cn.it8090.solrdemo.po.Products;
import cn.it8090.solrdemo.po.ProductsQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.List;

/**
 * @Description
 * @PoweredBy Amour Mars
 * @Date 20:26 2018/1/23
 */
public interface ProductSearchService {

    public List<Products> searchProducts(ProductsQuery productsQuery) throws Exception;

}
