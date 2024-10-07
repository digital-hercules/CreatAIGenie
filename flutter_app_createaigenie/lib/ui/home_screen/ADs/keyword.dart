import 'package:flutter/material.dart';

class KeywordsPage extends StatefulWidget {
  @override
  _KeywordsPageState createState() => _KeywordsPageState();
}

class _KeywordsPageState extends State<KeywordsPage> {
  // List to track the number for each keyword (you can modify the length based on your needs)
  List<int> keywordPrices = List<int>.generate(4, (index) => 250);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Keywords', style: TextStyle(color: Colors.black)),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: 16),
            _buildSearchBar(),
            SizedBox(height: 16),
            _buildSuggestedCompetitors(),
            SizedBox(height: 16),
            Expanded(
              child: ListView.builder(
                itemCount: keywordPrices.length, // Number of items
                itemBuilder: (context, index) {
                  return _buildKeywordCard(index); // Pass the index
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSearchBar() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        children: [
          Icon(Icons.search, color: Colors.grey),
          SizedBox(width: 10),
          Expanded(
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Search',
                border: InputBorder.none,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSuggestedCompetitors() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text(
          'Suggested',
          style: TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
        ),
        SizedBox(width: 10),
        Text(
          '|',
          style: TextStyle(color: Colors.grey),
        ),
        SizedBox(width: 10),
        Text(
          'Competitors',
          style: TextStyle(color: Colors.grey),
        ),
      ],
    );
  }

  // Build keyword card and update based on the index
  Widget _buildKeywordCard(int index) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 8),
      padding: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey[300]!),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Keyword',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
              SizedBox(height: 8),
              Text(
                'September 2024',
                style: TextStyle(color: Colors.grey),
              ),
            ],
          ),
          Row(
            children: [
              IconButton(
                icon: Icon(Icons.remove, color: Colors.purple),
                onPressed: () {
                  setState(() {
                    if (keywordPrices[index] > 0) {
                      keywordPrices[index]--; // Decrease the value
                    }
                  });
                },
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.purple),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Text(
                  '₹${keywordPrices[index]}', // Display updated value
                  style: TextStyle(
                      color: Colors.purple, fontWeight: FontWeight.bold),
                ),
              ),
              IconButton(
                icon: Icon(Icons.add, color: Colors.purple),
                onPressed: () {
                  setState(() {
                    keywordPrices[index]++; // Increase the value
                  });
                },
              ),
            ],
          ),
        ],
      ),
    );
  }
}
