import 'package:flutter/material.dart' show AppBar, BorderRadius, BoxDecoration, BuildContext, Color, Colors, Column, Container, CrossAxisAlignment, EdgeInsets, FontWeight, MainAxisAlignment, MaterialApp, Padding, Row, Scaffold, SingleChildScrollView, SizedBox, State, StatefulWidget, StatelessWidget, Text, TextStyle, Widget, runApp;
import 'package:syncfusion_flutter_charts/charts.dart'; // Syncfusion Charts for Donut Chart
import 'package:percent_indicator/percent_indicator.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: BudgetScreen(),
    );
  }
}

class BudgetScreen extends StatefulWidget {
  @override
  _BudgetScreenState createState() => _BudgetScreenState();
}

class _BudgetScreenState extends State<BudgetScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Budget'),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
      ),
      body: _buildBudgetPage(context), // Directly show the Budget page content
    );
  }

  Widget _buildBudgetPage(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: [
          SizedBox(height: 20),
          // Donut Chart Section
          Container(
            height: 200,
            child: SfCircularChart(
              series: <CircularSeries>[
                DoughnutSeries<ChartData, String>(
                  dataSource: getChartData(),
                  pointColorMapper: (ChartData data, _) => data.color,
                  xValueMapper: (ChartData data, _) => data.category,
                  yValueMapper: (ChartData data, _) => data.value,
                ),
              ],
            ),
          ),
          SizedBox(height: 20),
          // Total Spend Section
          Text(
            'Total Spend: Rs. 5000',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          SizedBox(height: 20),
          // Budget Usage Graph Section
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _buildBudgetCard('₹6780', 'ROAS', '2.76x', 11.75, true),
                _buildBudgetCard('₹6780', 'ROAS', '2.76x', 0, false),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // Budget Usage Card
  Widget _buildBudgetCard(String amount, String title, String value, double change, bool isIncrease) {
    return Container(
      width: 160,
      height: 120,
      decoration: BoxDecoration(
        color: Colors.purple.withOpacity(0.1),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: TextStyle(color: Colors.purple, fontWeight: FontWeight.bold),
            ),
            Text(
              value,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  amount,
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                if (isIncrease)
                  Text(
                    '+${change.toStringAsFixed(2)}%',
                    style: TextStyle(color: Colors.green),
                  )
                else
                  SizedBox(),
              ],
            ),
          ],
        ),
      ),
    );
  }

  // Dummy data for the chart
  List<ChartData> getChartData() {
    final List<ChartData> chartData = [
      ChartData('Category1', 25, Colors.green),
      ChartData('Category2', 35, Colors.blue),
      ChartData('Category3', 20, Colors.purple),
      ChartData('Category4', 20, Colors.deepPurple),
    ];
    return chartData;
  }
}

class ChartData {
  ChartData(this.category, this.value, this.color);
  final String category;
  final double value;
  final Color color;
}
