import 'package:flutter/material.dart';
import 'package:create_ai_genie/ui/home_screen/my_rewards/My_Rewards.dart';
import 'package:create_ai_genie/ui/home_screen/profile_screen/profile_screen.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:create_ai_genie/ui/home_screen/brand_voice_screen/brand_voice_screen.dart';
import 'package:create_ai_genie/ui/auth/login.dart'; // Import your LoginScreen here
import 'package:shared_preferences/shared_preferences.dart'; // Import SharedPreferences

class CustomDrawer extends StatelessWidget {
  const CustomDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
          topRight: Radius.circular(24),
          bottomRight: Radius.circular(24),
        ),
      ),
      child: Column(
        children: [
          DrawerHeader(
            decoration: BoxDecoration(
              color: const Color(0xffb219f0),
              borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.2),
                  blurRadius: 8,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Align(
                    alignment: Alignment.topRight,
                    child: GestureDetector(
                      onTap: () => Navigator.of(context).pop(),
                      child: const Icon(Icons.close, color: Colors.white, size: 30),
                    ),
                  ),
                  const Spacer(),
                  Row(
                    children: [
                      const CircleAvatar(
                        backgroundImage: AssetImage('assets/images/person.png'),
                        radius: 30,
                      ),
                      const SizedBox(width: 16),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('User Name', style: context.displayMedium?.copyWith(color: Colors.white)),
                          Text('General User', style: context.displaySmall?.copyWith(color: Colors.white)),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/p_icon.png',
                  title: 'Profile',
                  onTap: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const ProfileScreen())),
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/plan_icon.png',
                  title: 'Plans',
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/brand_icon.png',
                  title: 'Brand Voice',
                  onTap: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const BrandVoiceScreen())),
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/reward1_icon.png',
                  title: 'My Rewards',
                  onTap: () => Navigator.push(context, MaterialPageRoute(builder: (context) => const MyRewardsPage())),
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/feedback_icon.png',
                  title: 'Feedback',
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/faq_icon.png',
                  title: 'FAQ',
                ),
                _buildDrawerItem(
                  context,
                  icon: 'assets/icons/about_icon.png',
                  title: 'About App',
                ),
                const SizedBox(height: 20),
                _buildThemeSwitcher(context),
                const SizedBox(height: 20),
                _buildLogoutButton(context),
                const SizedBox(height: 16),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDrawerItem(BuildContext context, {required String icon, required String title, VoidCallback? onTap}) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 300),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(8),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.1),
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        margin: const EdgeInsets.symmetric(vertical: 4, horizontal: 16),
        child: ListTile(
          contentPadding: const EdgeInsets.symmetric(horizontal: 16),
          leading: Image.asset(icon, width: 24, height: 24),
          title: Text(title, style: context.displayMedium?.copyWith(fontSize: 18)),
          trailing: const Icon(Icons.arrow_forward_ios_rounded, color: Color(0xffb219f0)),
        ),
      ),
    );
  }

  Widget _buildThemeSwitcher(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text('Theme', style: context.displaySmall),
          Image.asset('assets/icons/switch_icon.png', width: 24, height: 24),
        ],
      ),
    );
  }

  Widget _buildLogoutButton(BuildContext context) {
    return Container(
      width: double.infinity,
      height: 50,
      margin: const EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        color: const Color(0xffF44B4B),
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 6,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: TextButton(
        onPressed: () async {
          final prefs = await SharedPreferences.getInstance();
          await prefs.remove('accessToken'); // Clear access token
          await prefs.remove('refreshToken'); // Clear refresh token

          // Debug print to check if tokens are removed
          print('Access Token removed: ${prefs.getString('accessToken')}');
          print('Refresh Token removed: ${prefs.getString('refreshToken')}');

          // Navigate to login page
          Navigator.of(context).pushReplacement(
            MaterialPageRoute(builder: (context) => const Login()),
          );
        },
        child: const Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('Log Out', style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.w500)),
            SizedBox(width: 8),
            Icon(Icons.logout, color: Colors.white),
          ],
        ),
      ),
    );
  }
}
