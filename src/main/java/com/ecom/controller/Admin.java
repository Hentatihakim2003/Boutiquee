package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; // Import pour StandardCopyOption
import java.util.List;
import java.nio.file.Path; // Import pour java.nio.file.Path

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Commande;
import com.ecom.model.Products;
import com.ecom.service.CommandeService;
import com.ecom.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class Admin {

    @Autowired
    private ProductService productService;
	@Autowired
    private CommandeService commandeService;

    /*@GetMapping("/home")
    public String adminHome(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "Admin"; // Retourne la vue admin
        } else {
            model.addAttribute("error", "Accès refusé");
            return "login"; // Redirige vers la page de connexion
        }
    }
*/
    @GetMapping("/Addproduct")
    public String addProductPage(HttpSession session, Model model) {
    	String role = (String) session.getAttribute("role");
    	if ("admin".equals(role)) {
    		return "Addproduct"; // Retourne la vue admin
        } else {
            model.addAttribute("error", "Accès refusé");
            return "login";
        }
        
    }

    @PostMapping("/Addproduct")
    public String addProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image,
            @RequestParam("stock") int stock,
            Model model) {
        try {
            if (image.isEmpty()) {
                System.out.println("Erreur: L'image est vide !");
                return "redirect:/Admin/Addproduct";
            }

            String fileName = image.getOriginalFilename();

            // Créer l'objet produit
            Products product = new Products();
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            product.setImage(fileName);
            product.setStock(stock);

            // Appeler la méthode pour créer le produit
            productService.createProduct(product);

            String path = "src/main/resources/static/img/product_img"; // Chemin relatif dans votre projet
            File saveDir = new File(path);

            if (!saveDir.exists()) {
                if (saveDir.mkdirs()) {
                    System.out.println("Répertoire créé : " + saveDir.getAbsolutePath());
                } else {
                    System.out.println("Erreur lors de la création du répertoire.");
                    model.addAttribute("error", "Erreur lors de la création du répertoire.  ");
                    return "Addproduct";
                }
            }
            Path imagePath = Paths.get(saveDir.getAbsolutePath(), fileName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            return "redirect:/Admin/Addproduct";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du téléchargement de l'image: ");
            System.out.println("" + e.getMessage());
            return "Addproduct";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur imprévue:  ");
            System.out.println("Erreur imprévue: " + e.getMessage());
            return "Addproduct";
        }
    }
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam Long orderId, @RequestParam Commande.Status status) {
    	commandeService.updateStatus(orderId, status);
        return "redirect:/Admin/Gerercmd"; // Redirigez vers la liste des commandes
    }
    @GetMapping("/Gerercmd")
    public String getAllCommandes(Model model) {
        List<Commande> commandes = commandeService.getAllCommandes();
        model.addAttribute("cmd", commandes);
        return "ManageOrders"; // Nom du fichier HTML dans le dossier templates
    }

}
