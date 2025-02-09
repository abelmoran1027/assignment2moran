package com.csc340.pokemon;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestApiController {
    Map<Integer, Pokemon> pokeDex = new HashMap<>();


    @GetMapping("/hello")
    public String hello() {
        return "Hello, this is your PokeDex." ;
    }

    @GetMapping("/pokemon/all")
    public Object getAllDiscoveredPoke(){
        if (pokeDex.isEmpty()){
            pokeDex.put(1, new Pokemon("Bulbasaur"));
        }
        return pokeDex.values();
    }

    @GetMapping("/pokemon")
    public Object getPokemon() {
        try {
            //CONSUMING A RESTFUL WEB SERVICE (API)
            String url = "https://pokeapi.co/api/v2/pokemon/";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);

            List<Pokemon> pokemonList = new ArrayList<>();

            JsonNode results = root.get("results");
            for (JsonNode rt : results) {
                
                String name = rt.get("name").asText();

                Pokemon pokemon = new Pokemon(name);
                pokemonList.add(pokemon);
            }
            return pokemonList;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /pokemon";
        }
    }






}
