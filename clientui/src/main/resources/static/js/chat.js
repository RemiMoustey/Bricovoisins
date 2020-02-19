class Chat {
    constructor() {
        this.messageForm = $('#messageForm');
        this.messageInput = $('#message');
        this.messageArea = $('#messageArea');
        this.connectingElement = $('#connecting');

        this.stompClient = null;
        this.username = null;

        this.connect();

        this.messageForm.on('submit', this.sendMessage.bind(this));
    }

    // Connect to WebSocket Server.
    connect() {
        this.username = $('#username').text().trim();

        let socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, this.onConnected.bind(this), this.onError.bind(this));
    }

    onConnected() {
        // Subscribe to the Public Topic
        this.stompClient.subscribe('/topic/publicChatRoom', this.onMessageReceived.bind(this));

        // Tell your username to the server
        this.stompClient.send("/app/chat.addUser",
            {},
            JSON.stringify({sender: this.username, type: 'JOIN'})
        );

        this.connectingElement.addClass('hidden');
    }

    onError() {
        this.connectingElement.text('Could not connect to WebSocket server. Please refresh this page to try again!');
        this.connectingElement.css('color', 'red');
    }

    sendMessage(event) {
        let messageContent = this.messageInput.val().trim();
        if (messageContent && this.stompClient) {
            let chatMessage = {
                sender: this.username,
                content: this.messageInput.val(),
                type: 'CHAT'
            };
            this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            this.messageInput.val('');
        }
        event.preventDefault();
    }

    onMessageReceived(payload) {
        let message = JSON.parse(payload.body);

        let messageElement = document.createElement('li');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' a rejoint le chat.';
            messageElement.style.color = '#0c991a';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' a quitté le chat.';
            messageElement.style.color = '#0c991a';
        } else {
            messageElement.classList.add('chat-message');
            let usernameElement = document.createElement('strong');
            usernameElement.classList.add('nickname');
            let usernameText = document.createTextNode(message.sender + " : ");
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        let textElement = document.createElement('span');
        let messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        this.messageArea.append(messageElement);
        this.messageArea.scrollTop = this.messageArea.scrollHeight;
    }
}

new Chat();