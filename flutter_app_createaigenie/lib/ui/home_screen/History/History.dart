    import 'package:flutter/material.dart';

    void main() {
      runApp(MyApp());
    }

    class MyApp extends StatelessWidget {
      @override
      Widget build(BuildContext context) {
        return MaterialApp(
          home: HistoryScreen(),
        );
      }
    }

    class HistoryScreen extends StatelessWidget {
      @override
      Widget build(BuildContext context) {
        return Scaffold(
          appBar: AppBar(
            title: Text('History Screen'),
          ),
          body: Container(
            color: Colors.white, // Blank white page
          ),
        );
      }
    }
