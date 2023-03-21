import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8001",
  withCredentials: false,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
});

export default {
  getCart(config) {
    return apiClient.get("/cart", config).then((response) => {
      console.log(response.data);
      return response.data;
    });
  },
  getUser(user) {
    return apiClient
      .post("/login", null, {
        params: {
          username: user.username,
          password: user.password,
        },
      })
      .then((response) => {
        console.log(response);
        return response.data;
      });
  },

  getToken(user) {
    return apiClient
      .post("/token", null, {
        params: {
          username: user.username,
          password: user.password,
        },
      })
      .then((response) => {
        return response.data;
      });
  },

  postUser(user) {
    return apiClient
      .post("/register", null, {
        params: {
          username: user.username,
          password: user.password,
          email: user.email,
        },
      })
      .then((response) => {
        return response.data;
      });
  },
};