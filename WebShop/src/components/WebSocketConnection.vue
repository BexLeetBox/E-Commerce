<template>
    <div>
      <div v-for="message in messages" :key="message.id">{{ message.content }}</div>
      <input v-model="input" @keyup.enter="sendMessage" />
    </div>
</template>

<script>
export default {
data() {
    return {
    input: "",
    messages: [],
    socket: null,
    };
},

mounted() {
    this.socket = new WebSocket("ws://localhost:8001/chat");
    this.socket.onmessage = (event) => {
    const message = JSON.parse(event.data);
    this.messages.push(message);
    };
},

methods: {
    sendMessage() {
    const message = {
        content: this.input,
    };
    this.socket.send(JSON.stringify(message));
    this.input = "";
    },
},
};
</script>