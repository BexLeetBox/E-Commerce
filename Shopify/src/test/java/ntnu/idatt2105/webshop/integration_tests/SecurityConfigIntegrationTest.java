package ntnu.idatt2105.webshop.integration_tests;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginEndpointIsAccessible() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "testuser")
                        .param("password", "testpassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void testProtectedEndpointIsInaccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sell-item"))
                .andExpect(status().isForbidden());
    }


    @Test
    public void testProtectedEndpointIsAccessibleWithAuthentication() throws Exception {

        JSONObject userJson = new JSONObject();
        userJson.put("username", "s");
        userJson.put("password", "s");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(response);
        String token = jsonObject.getString("token");

        long productId = 2; // Replace 1L with an actual product ID

        mockMvc.perform(MockMvcRequestBuilders.post("/addToCart")
                        .header("Authorization", "Bearer " + token)
                        .param("id", String.valueOf(productId)))
                .andExpect(status().isOk());
    }



}
