package ntnu.idatt2105.WebShop.controller;


import ntnu.idatt2105.WebShop.model.Cart;
import ntnu.idatt2105.WebShop.model.Product;
import ntnu.idatt2105.WebShop.model.User;
import ntnu.idatt2105.WebShop.repositories.CartRepository;
import ntnu.idatt2105.WebShop.repositories.ProductRepository;
import ntnu.idatt2105.WebShop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin()
@RestController
public class RestApiController {

    Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;


    @CrossOrigin()
    @RequestMapping(value="/sell-item", method=RequestMethod.POST)
    public Map<String, String> process(@RequestBody Map <String, Object> req,@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {


                Map<String, String> returnObject = new HashMap<>();
                authentication.getName();
                User user = userRepository.findByUsername(authentication.getName());
                String image = Base64.getEncoder().encodeToString(file.getBytes());
                Product product = new Product((String) req.get("briefDescription"), (String) req.get("fullDescription"), (String) req.get("category"),
                        (Double) req.get("latitude"), (Double) req.get("longitude"), (Double) req.get("price"), image);
                logger.info("Sending cart of user "+user.getUsername());
                productRepository.save(product);

                return returnObject;
            }


           /** else{
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client is not properly authenticated");
            }
        Map<String, String> returnObject = new HashMap<>();
        returnObject.put("history", "Not authorized");
        returnObject.put("answer", "Not authorized");
        return returnObject;}*/



    /**@CrossOrigin()
    @RequestMapping(value="/cart", method=RequestMethod.GET)
    public Map<String, String> process(Authentication authentication) {

        if (authentication!=null){
            if (authentication.isAuthenticated()){
                Map<String, String> returnObject = new HashMap<>();
                logger.info("Sending cart: ");
                String answer = calc.doCalc();
                String history = calc.getHistory();
                returnObject.put("answer", answer);
                returnObject.put("history", history);
                logger.info("Answer: " + answer);
                authentication.getName();
                User user = userRepository.findByUsername(authentication.getName());
                calculationRepository.save(new Calculation(history, user));
                return returnObject;
            }
        }
        Map<String, String> returnObject = new HashMap<>();
        returnObject.put("history", "Not authorized");
        returnObject.put("answer", "Not authorized");
        return returnObject;
    }

    @CrossOrigin
    @RequestMapping(value = "/history", method=RequestMethod.GET)
    public ArrayList<String> getHistory(Authentication authentication){
        if (authentication != null || true) {
            User user = userRepository.findByUsername(authentication.getName());
            logger.info("Sending all calculations by: "+user.getUsername());
            ArrayList<Calculation> arr= calculationRepository.findCalculationsByUser(user);
            ArrayList<String> strings = new ArrayList<>();
            for(Calculation calc: arr){
                strings.add(calc.getCalculation());
            }
            return strings;
        } else {
            return null;
        }
    }*/
}