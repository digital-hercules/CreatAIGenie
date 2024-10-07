import 'package:create_ai_genie/ui/home_screen/ADs/ADs.dart';
import 'package:create_ai_genie/ui/home_screen/History/History.dart';
import 'package:create_ai_genie/ui/home_screen/ai_assistant/ai_assistant_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'appbar.dart';
import 'home_screen.dart';

class Home extends StatefulWidget {
  static const routeName = '/home';

  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int cIndex = 0;

  // Updated list with the actual screens for AI Assistant, ADs, and History
  List<Widget> screens = [
    HomeScreen(),
    AIAssistantScreen(),  // AI Assistant screen
    ADs(),                // ADs screen
    HistoryScreen()        // History screen (replace with actual History screen if different)
  ];

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: Colors.white,
        body: screens[cIndex],  // This will display the appropriate screen

        bottomNavigationBar: FABBottomAppBar(
          centerItemText: '',
          color: Color(0xff808080),
          backgroundColor: Colors.white,
          selectedColor: Colors.black,
          notchedShape: CircularNotchedRectangle(),
          onTabSelected: (index) {
            setState(() {
              cIndex = index;
            });
          },
          items: [
            FABBottomAppBarItem(iconData: 'assets/images/h_i.png', text: 'Home'),
            FABBottomAppBarItem(iconData: 'assets/icons/ai_a.png', text: 'AI Assistant'),
            FABBottomAppBarItem(iconData: 'assets/images/ad_2.jpeg', text: 'ADs'),
            FABBottomAppBarItem(iconData: 'assets/images/h_hes.png', text: 'History'),
          ],
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
        floatingActionButton: FloatingActionButton(
          backgroundColor: Colors.black,
          onPressed: () {},
          child: Icon(Icons.add, color: Colors.white, size: 40),
          elevation: 0,
        ),
      ),
    );
  }
}
