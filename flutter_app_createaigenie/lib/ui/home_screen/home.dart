import 'package:aigeniog/ui/home_screen/ai_assistant/ai_assistant_screen.dart';
import 'package:aigeniog/ui/home_screen/chat_page/chat_pagescreen.dart';
import 'package:flutter/material.dart';

import 'appbar.dart'; // Ensure this file exists and contains the correct imports for FABBottomAppBar and FABBottomAppBarItem
import 'home_screen.dart'; // Ensure this file contains the HomeScreen widget

class Home extends StatefulWidget {
  static const routeName = '/home';

  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int cIndex = 0;
  final List<Widget> screens = [
    const HomeScreen(),
    const HomeScreen(),
    const HomeScreen(),
    const HomeScreen(),
  ];

  void _onTabSelected(int index) {
    setState(() {
      cIndex = index;
    });

    if (index == 1) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const AIAssistantScreen()),
      );
    } else if (index == 2) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => ChatScreenpage()),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: Colors.white,
        body: screens[cIndex],
        bottomNavigationBar: FABBottomAppBar(
          centerItemText: '',
          color: const Color(0xff808080),
          backgroundColor: Colors.white,
          selectedColor: Colors.black,
          notchedShape: const CircularNotchedRectangle(),
          onTabSelected: _onTabSelected,
          items: [
            FABBottomAppBarItem(
              iconData: 'assets/images/h_i.png',
              text: 'Home',
            ),
            FABBottomAppBarItem(
              iconData: 'assets/icons/ai_a.png',
              text: 'AI Assistant',
            ),
            FABBottomAppBarItem(
              iconData: 'assets/images/h_c.png',
              text: 'Chat',
            ),
            FABBottomAppBarItem(
              iconData: 'assets/images/h_hes.png',
              text: 'History',
            ),
          ],
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
        floatingActionButton: FloatingActionButton(
          backgroundColor: Colors.black,
          onPressed: () {
            // Add action for FAB here
            print('Floating Action Button Pressed');
          },
          elevation: 0,
          child: const Icon(Icons.add, color: Colors.white, size: 40),
        ),
      ),
    );
  }
}
