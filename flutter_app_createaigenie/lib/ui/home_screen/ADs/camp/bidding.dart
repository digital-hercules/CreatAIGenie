import 'package:flutter/material.dart';

class BiddingPg extends StatelessWidget {
  const BiddingPg({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            // Add your back navigation logic here
          },
        ),
        title: const Text('AD Campaign'),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Center(
              child: Text(
                'Bidding Strategy',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: Colors.purple,
                ),
              ),
            ),
            const SizedBox(height: 20),
            const Center(
              child: CircleAvatar(
                radius: 40,
                backgroundColor: Colors.purple,
                child: Text(
                  'AD',
                  style: TextStyle(fontSize: 24, color: Colors.white),
                ),
              ),
            ),
            const SizedBox(height: 30),
            _buildKeywordCard(context, 'September 2024', 250),
            const SizedBox(height: 16),
            _buildKeywordCard(context, 'September 2024', 250),
            const SizedBox(height: 30),
            const Text(
              'Budget Usage Graph',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
            ),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _buildAdMetricCard('Conversion Rate', '2.76x', '₹6780', '11.75%'),
                _buildAdMetricCard('Click Through Rate', '2.76x', '₹6780', '11.75%'),
              ],
            ),
            const Spacer(),
            Center(
              child: ElevatedButton(
                onPressed: () {
                  // Action for Create New button
                  print("Create Now Clicked!");
                },
                style: ElevatedButton.styleFrom(
                  foregroundColor: Colors.black, backgroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                    side: const BorderSide(color: Colors.black),
                  ),
                  padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 16),
                ),
                child: const Text(
                  'Create New',
                  style: TextStyle(fontSize: 16),
                ),
              ),
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }

  Widget _buildKeywordCard(BuildContext context, String keyword, double price) {
    return Card(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(8),
        side: const BorderSide(color: Colors.grey),
      ),
      elevation: 0,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Keyword',
              style: Theme.of(context).textTheme.titleMedium, // Updated from subtitle1
            ),
            Text(
              keyword,
              style: Theme.of(context).textTheme.bodySmall, // Updated from caption
            ),
            const SizedBox(height: 10),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    IconButton(
                      onPressed: () {},
                      icon: const Icon(Icons.remove_circle_outline, color: Colors.purple),
                    ),
                    Text(
                      '₹$price',
                      style: const TextStyle(fontSize: 18, color: Colors.purple),
                    ),
                    IconButton(
                      onPressed: () {},
                      icon: const Icon(Icons.add_circle_outline, color: Colors.purple),
                    ),
                  ],
                ),
                TextButton(
                  onPressed: () {},
                  child: const Text('Edit', style: TextStyle(color: Colors.purple)),
                )
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAdMetricCard(String title, String value, String amount, String percentChange) {
    return Container(
      width: 150,
      decoration: BoxDecoration(
        color: Colors.purple.shade50,
        borderRadius: BorderRadius.circular(16),
      ),
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: const TextStyle(fontSize: 14, color: Colors.purple),
          ),
          const SizedBox(height: 10),
          Text(
            value,
            style: const TextStyle(fontSize: 24, color: Colors.purple, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 10),
          Text(
            amount,
            style: const TextStyle(fontSize: 18),
          ),
          Text(
            percentChange,
            style: const TextStyle(fontSize: 14, color: Colors.green),
          ),
        ],
      ),
    );
  }
}

void main() {
  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: const BiddingPg(),
  ));
}
