document.addEventListener('DOMContentLoaded', function() {
    const chatbotButton = document.getElementById('chatbotButton');
    const chatbotWindow = document.getElementById('chatbotWindow');
    const chatbotClose = document.getElementById('chatbotClose');
    const chatbotMessages = document.getElementById('chatbotMessages');
    const userInput = document.getElementById('userInput');
    const sendButton = document.getElementById('sendButton');
    
    // Toggle chatbot window
    chatbotButton.addEventListener('click', function() {
        chatbotWindow.classList.toggle('active');
        if (chatbotWindow.classList.contains('active')) {
            userInput.focus();
        }
    });
    
    chatbotClose.addEventListener('click', function() {
        chatbotWindow.classList.remove('active');
    });
    
    // Send message function
    function sendMessage() {
        const message = userInput.value.trim();
        if (message) {
            addMessage(message, 'user');
            userInput.value = '';
            
            // Show typing indicator
            showTypingIndicator();
            
            // Send to Rasa server
            fetch('http://localhost:8083/chat/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    sender: 'user',
                    message: message
                })
            })
            .then(response => response.json())
            .then(data => {
                // Remove typing indicator
                removeTypingIndicator();
                
                // Process responses
                data.forEach(response => {
                    if (response.text) {
                        addMessage(response.text, 'bot');
                    }
                    
                    if (response.buttons) {
                        addButtons(response.buttons);
                    }
                    
                    if (response.image) {
                        addImage(response.image);
                    }
                });
            })
            .catch(error => {
                removeTypingIndicator();
                addMessage("Sorry, I'm having trouble connecting to the server. Please try again later.", 'bot');
                console.error('Error:', error);
            });
        }
    }
    
    // Add message to chat
    function addMessage(text, sender) {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message', sender + '-message');
        
        const messageText = document.createElement('div');
        messageText.textContent = text;
        messageDiv.appendChild(messageText);
        
        // Add timestamp
        const timestamp = document.createElement('div');
        timestamp.classList.add('timestamp');
        timestamp.textContent = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        messageDiv.appendChild(timestamp);
        
        chatbotMessages.appendChild(messageDiv);
        chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
    }
    
    // Add buttons to chat
    function addButtons(buttons) {
        const buttonsContainer = document.createElement('div');
        buttonsContainer.classList.add('quick-reply');
        
        buttons.forEach(button => {
            const buttonElement = document.createElement('button');
            buttonElement.classList.add('quick-reply-button');
            buttonElement.textContent = button.title;
            buttonElement.addEventListener('click', () => {
                addMessage(button.title, 'user');
                buttonsContainer.remove();
                
                // Show typing indicator
                showTypingIndicator();
                
                // Send payload to Rasa
                fetch('http://localhost:8083/chat/send', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        sender: 'user',
                        message: button.payload || button.title
                    })
                })
                .then(response => response.json())
                .then(data => {
                    removeTypingIndicator();
                    data.forEach(response => {
                        if (response.text) {
                            addMessage(response.text, 'bot');
                        }
                        
                        if (response.buttons) {
                            addButtons(response.buttons);
                        }
                    });
                });
            });
            buttonsContainer.appendChild(buttonElement);
        });
        
        chatbotMessages.appendChild(buttonsContainer);
        chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
    }
    
    // Add image to chat
    function addImage(imageUrl) {
        const imageDiv = document.createElement('div');
        imageDiv.classList.add('message', 'bot-message');
        
        const imageElement = document.createElement('img');
        imageElement.src = imageUrl;
        imageElement.style.maxWidth = '100%';
        imageElement.style.borderRadius = '8px';
        imageDiv.appendChild(imageElement);
        
        chatbotMessages.appendChild(imageDiv);
        chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
    }
    
    // Show typing indicator
    function showTypingIndicator() {
        const typingDiv = document.createElement('div');
        typingDiv.classList.add('message', 'bot-message', 'typing-indicator');
        typingDiv.id = 'typingIndicator';
        
        for (let i = 0; i < 3; i++) {
            const dot = document.createElement('div');
            dot.classList.add('typing-dot');
            typingDiv.appendChild(dot);
        }
        
        chatbotMessages.appendChild(typingDiv);
        chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
    }
    
    // Remove typing indicator
    function removeTypingIndicator() {
        const typingIndicator = document.getElementById('typingIndicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }
    
    // Event listeners for sending messages
    sendButton.addEventListener('click', sendMessage);
    userInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });
    
    // Initial bot greeting
    setTimeout(() => {
        if (chatbotMessages.children.length === 0) {
            addMessage("Hello! I'm your Land Records Assistant. How can I help you today?", 'bot');
        }
    }, 500);
});