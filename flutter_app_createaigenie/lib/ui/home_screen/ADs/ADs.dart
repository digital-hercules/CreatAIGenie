import 'package:create_ai_genie/ui/home_screen/ADs/keyword.dart';
import 'package:create_ai_genie/ui/home_screen/ADs/budget_page.dart';
import 'package:create_ai_genie/ui/home_screen/ADs/campaign_page.dart';

   // Import your BudgetPage
import 'package:flutter/material.dart';

import '../drawer.dart';

class ADs extends StatefulWidget {
  @override
  _ADsState createState() => _ADsState();
}

class _ADsState extends State<ADs> {
  // Default dropdown value
  String selectedRange = '7 Days';

  // Variable to track the selected tab
  String selectedTab = 'Home';

  // Function to get the current tab content based on the selected tab
  Widget _getCurrentTabContent() {
    switch (selectedTab) {
      case 'Keywords':
        return KeywordsPage(); // Replace with your actual KeywordsPage widget
      case 'Campaign':
        return CampaignApp(); // Replace with your actual CampaignPage widget
      case 'Budget':
        return BudgetScreen(); // Replace with your actual BudgetPage widget
      case 'Home':
      default:
        return _buildHomeTab(); // The default home tab content
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: Builder(
          builder: (context) => IconButton(
            icon: Icon(Icons.menu, color: Colors.black), // Drawer icon
            onPressed: () => Scaffold.of(context).openDrawer(), // Open the drawer
          ),
        ),
        title: Text('AD Campaign'),
        backgroundColor: Colors.white,
        elevation: 0,
        centerTitle: true, // Keeps the title centered
        titleTextStyle: TextStyle(
          color: Colors.black,
          fontWeight: FontWeight.bold,
          fontSize: 24,
        ),
      ),
      drawer: const CustomDrawer(),


      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Welcome Text
            Text(
              'Welcome, Taranpreet!',
              style: TextStyle(
                color: Colors.purple,
                fontSize: 22,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 10),

            // Row for "Ad Campaign Performance" and Dropdown on the same line
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                // Ad Campaign Performance Text
                Text(
                  'Ad Campaign Performance',
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                // Dropdown Filter Tab on the same line, right-aligned
                _buildDropdownFilterTab(),
              ],
            ),
            SizedBox(height: 10),

            // Tabs (Home, Keywords, Campaign, Budget)
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _buildFilterTab('Home', selectedTab == 'Home'),
                _buildFilterTab('Keywords', selectedTab == 'Keywords'),
                _buildFilterTab('Campaign', selectedTab == 'Campaign'),
                _buildFilterTab('Budget', selectedTab == 'Budget'),
              ],
            ),
            SizedBox(height: 20),

            // Display the content based on the selected tab
            Expanded(
              child: _getCurrentTabContent(),
            ),
          ],
        ),
      ),
    );
  }

  // Function to build Filter Tab with onTap functionality
  Widget _buildFilterTab(String label, bool isSelected) {
    return InkWell(
      onTap: () {
        setState(() {
          selectedTab = label;  // Update the selected tab when clicked
        });
      },
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? Colors.purple : Colors.white,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(color: Colors.black26),
        ),
        child: Text(
          label,
          style: TextStyle(
            color: isSelected ? Colors.white : Colors.black,
            fontSize: 14,
          ),
        ),
      ),
    );
  }

  // Build the Home tab content
  Widget _buildHomeTab() {
    return Column(
      children: [
        // Statistic Cards
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _buildStatCard('15.2%', 'Advertising\nCost of Sale'),
            _buildStatCard('3.8x', 'Return on\nAd Spend'),
          ],
        ),
        SizedBox(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _buildStatCard('14.5%', 'Click-Through\nRate'),
            _buildStatCard('\$2500', 'Total Ad\nSpend'),
          ],
        ),
        SizedBox(height: 20),

        // Performance Trend Chart Placeholder
        Text(
          'Performance Trend Chart',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        SizedBox(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _buildTrendCard('ROAS', '₹6780', 11.75),
            _buildTrendCard('ROAS', '₹6780', 11.75),
          ],
        ),
      ],
    );
  }

  // Function to build Statistic Card
  Widget _buildStatCard(String value, String description) {
    return Container(
      width: 150,
      padding: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.purple,
        borderRadius: BorderRadius.circular(15),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            value,
            style: TextStyle(
              color: Colors.white,
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: 5),
          Text(
            description,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
            ),
          ),
        ],
      ),
    );
  }

  // Function to build Trend Card
  Widget _buildTrendCard(String title, String amount, double percentage) {
    return Container(
      width: 150,
      padding: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.purple[50],
        borderRadius: BorderRadius.circular(15),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            title,
            style: TextStyle(
              color: Colors.black,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: 10),
          Text(
            amount,
            style: TextStyle(
              color: Colors.black,
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: 5),
          Text(
            '$percentage%',
            style: TextStyle(
              color: Colors.green,
              fontSize: 16,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDropdownFilterTab() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: Colors.grey[300], // Ash color
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: Colors.black26),
      ),
      child: SizedBox(
        width: 46,
        height: 20,
        child: DropdownButton<String>(
          value: selectedRange,
          underline: SizedBox(), // Removes the underline
          icon: Icon(Icons.arrow_drop_down, color: Colors.black),
          isExpanded: false,
          items: ['7 Days', '14 Days', '30 Days'].map((String value) {
            return DropdownMenuItem<String>(
              value: value,
              child: Text(value),
            );
          }).toList(),
          onChanged: (String? newValue) {
            setState(() {
              selectedRange = newValue!;
            });
          },
          style: TextStyle(color: Colors.black, fontSize: 14),
        ),
      ),
    );
  }
}
