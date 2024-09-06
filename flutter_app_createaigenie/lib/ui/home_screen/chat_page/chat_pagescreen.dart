import 'package:flutter/material.dart';

class ChatScreenpage extends StatefulWidget {
  const ChatScreenpage({super.key});

  @override
  _ChatScreenpageState createState() => _ChatScreenpageState();
}

class _ChatScreenpageState extends State<ChatScreenpage> {
  final List<String> _messages = [
    'Hi Sanju!',
    'hello AI Genie',
  ];
  final TextEditingController _messageController = TextEditingController();
  final TextEditingController _keywordController = TextEditingController();

  void _handleKeywordSubmit(String message) {
    if (message.trim().isNotEmpty) {
      setState(() {
        _messages.add(message);
        _keywordController.clear(); // Clear the input field
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        backgroundColor: Colors.black,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.volume_up, color: Colors.white),
            onPressed: () {
              // Implement the functionality to read aloud the text on the screen
            },
          ),
        ],
      ),
      backgroundColor: Colors.black,
      body: Column(
        children: [
          // Chat messages
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.all(16.0),
              itemCount: _messages.length,
              itemBuilder: (context, index) {
                return ChatBubble(
                  message: _messages[index],
                  isBot: index == 0, 
                );
              },
            ),
          ),
          // Switch to Web and Chat input box
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          const Text(
                            'Switch to Web',
                            style: TextStyle(color: Colors.white),
                          ),
                          Switch(
                            value: false,
                            onChanged: (bool value) {},
                            activeColor: Colors.green,
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Container(
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.white, width: 1),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Output In',
                                style: TextStyle(color: Colors.white, fontSize: 20),
                              ),
                              DropdownButton<String>(
                                value: 'English',
                                icon: const Icon(Icons.arrow_drop_down, color: Colors.white),
                                dropdownColor: Colors.black,
                                items: <String>['English', 'Spanish', 'French', 'French', 'Galician', 'German', 'Greek', 'Hindi']
                                    .map((String value) {
                                  return DropdownMenuItem<String>(
                                    value: value,
                                    child: Text(value, style: const TextStyle(color: Colors.white)),
                                  );
                                }).toList(),
                                onChanged: (String? newValue) {},
                              ),
                              const SizedBox(height: 8),
                              const Text(
                                'Writing Style',
                                style: TextStyle(color: Colors.white, fontSize: 20),
                              ),
                              DropdownButton<String>(
                                value: 'Google Sans',
                                icon: const Icon(Icons.arrow_drop_down, color: Colors.white),
                                dropdownColor: Colors.black,
                                items: <String>['Google Sans', 'Roboto', 'Product Sans']
                                    .map((String value) {
                                  return DropdownMenuItem<String>(
                                    value: value,
                                    child: Text(value, style: const TextStyle(color: Colors.white)),
                                  );
                                }).toList(),
                                onChanged: (String? newValue) {},
                              ),
                            ],
                          ),
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Tone',
                                style: TextStyle(color: Colors.white, fontSize: 20),
                              ),
                              DropdownButton<String>(
                                value: 'Normal',
                                icon: const Icon(Icons.arrow_drop_down, color: Colors.white),
                                dropdownColor: Colors.black,
                                items: <String>['Normal', 'Friendly', 'Formal', 'Casual', 'Professional', 'informative', 'caring', 'Direct']
                                    .map((String value) {
                                  return DropdownMenuItem<String>(
                                    value: value,
                                    child: Text(value, style: const TextStyle(color: Colors.white)),
                                  );
                                }).toList(),
                                onChanged: (String? newValue) {},
                              ),
                              const SizedBox(height: 8),
                              const Text(
                                'Keyword',
                                style: TextStyle(color: Colors.white, fontSize: 20),
                              ),
                              const SizedBox(
                                width: 160.0, // Adjust the width as needed
                                child: TextField(
                                  decoration: InputDecoration(
                                    filled: true,
                                    fillColor: Colors.white,
                                    border: OutlineInputBorder(),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Row(
                            children: [
                              const Text(
                                'You have 5 messages left.',
                                style: TextStyle(color: Colors.white),
                                textAlign: TextAlign.center,
                              ),
                              GestureDetector(
                                onTap: () {
                                  // Handle "Get More Messages" action
                                },
                                child: const Text(
                                  'Get More Messages',
                                  style: TextStyle(color: Colors.orange),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: _messageController,
                        style: const TextStyle(color: Colors.white),
                        decoration: InputDecoration(
                          filled: true,
                          fillColor: Colors.grey[900],
                          hintText: 'Write your message',
                          hintStyle: const TextStyle(color: Colors.grey),
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(15),
                            borderSide: BorderSide.none,
                          ),
                        ),
                        onSubmitted: (String text) {
                          // Handle the message input
                          setState(() {
                            _messages.add(text);
                            _messageController.clear();
                          });
                        },
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.camera_alt, color: Colors.white),
                      onPressed: () {},
                    ),
                    IconButton(
                      icon: const Icon(Icons.mic, color: Colors.white),
                      onPressed: () {},
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class ChatBubble extends StatelessWidget {
  final String message;
  final bool isBot;

  const ChatBubble({super.key, required this.message, required this.isBot});

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: isBot ? Alignment.centerLeft : Alignment.centerRight,
      child: Row(
        mainAxisAlignment: isBot ? MainAxisAlignment.start : MainAxisAlignment.end,
        children: [
          Container(
            margin: const EdgeInsets.symmetric(vertical: 8.0),
            padding: const EdgeInsets.all(16.0),
            decoration: BoxDecoration(
              color: isBot ? Colors.white : Colors.blueAccent,
              borderRadius: BorderRadius.circular(12),
            ),
            child: Text(
              message,
              style: TextStyle(
                color: isBot ? Colors.black : Colors.white,
              ),
            ),
          ),
          const SizedBox(width: 8), // Space between chat bubble and icon
          if (!isBot) // Show icon only for user messages
            const Icon(Icons.person, color: Colors.white), // Replace with your desired icon
        ],
      ),
    );
  }
}
