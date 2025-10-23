package com.login.pageLogin.controller;

import com.login.pageLogin.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller
@RequestMapping("/produto")
public class ProdutoViewController {

    @Autowired
    private RestTemplate restTemplate;

    // A URL base da nossa API de produtos
    private final String API_URL = "http://localhost:8080/api/product";

    // LISTAR: Busca os dados da API
    @GetMapping
    public String listarProdutos(Model model) {
        // Chama GET /api/product e espera uma lista de produtos
        Product[] produtos = restTemplate.getForObject(API_URL, Product[].class);

        model.addAttribute("produtos", produtos);
        model.addAttribute("produto", new Product()); // Para o formulário de adição
        return "produtos";
    }

    // SALVAR (Criar ou Editar): Envia os dados para a API
    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Product produto) {
        UUID id = produto.getId();
        if (id == null) {
            // CRIAR: Chama POST /api/product
            restTemplate.postForObject(API_URL, produto, Product.class);
        } else {
            // EDITAR: Chama PUT /api/product/{id}
            restTemplate.put(API_URL + "/" + id, produto);
        }
        return "redirect:/produto";
    }

    // MOSTRAR FORMULÁRIO DE EDIÇÃO: Busca um produto da API para preencher
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") UUID id, Model model) {
        // Chama GET /api/product/{id}
        Product produto = restTemplate.getForObject(API_URL + "/" + id, Product.class);

        // Chama GET /api/product para a lista da tabela
        Product[] produtos = restTemplate.getForObject(API_URL, Product[].class);

        model.addAttribute("produto", produto); // Produto a ser editado
        model.addAttribute("produtos", produtos); // Lista para a tabela
        return "produtos";
    }

    // DELETAR: Manda a requisição de deleção para a API
    @GetMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable("id") UUID id) {
        // Chama DELETE /api/product/{id}
        restTemplate.delete(API_URL + "/" + id);
        return "redirect:/produto";
    }
}