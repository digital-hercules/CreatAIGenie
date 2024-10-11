import 'package:flutter/material.dart';
import 'package:hugeicons/hugeicons.dart';
import 'package:create_ai_genie/ui/home_screen/notification/notification_screen.dart';
import 'package:create_ai_genie/utils/extensions/screen_util_extension.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'drawer.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}



class _HomeScreenState extends State<HomeScreen> {

  //tutorial keys
  final GlobalKey _floatingButtonKey = GlobalKey();
  final GlobalKey _editButtonKey = GlobalKey();
  final GlobalKey _settingsButtonKey = GlobalKey();


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          'Create AI Genie',
          style: context.displayMedium?.copyWith(
            fontSize: 20,
            fontWeight: FontWeight.w600,
          ),
        ),
        actions: [
          _buildIconAction(
            context,
            HugeIcons.strokeRoundedSchoolBell01,
            const NotificationScreen(),
          ),
          _buildIconAction(
            context,
            HugeIcons.strokeRoundedSetting07,
            const NotificationScreen(),
          ),
        ],
        elevation: 0,
        backgroundColor: Colors.white,
      ),
      drawer: const CustomDrawer(),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildGreetingCard(context),
              const SizedBox(height: 16),
              _buildFeaturedList(),
              const SizedBox(height: 16),
              _buildFeatureSection(context),
              const SizedBox(height: 24),
              _buildAdditionalFeatures(context),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildIconAction(BuildContext context, IconData icon, Widget destination) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => destination),
        );
      },
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: HugeIcon(
          icon: icon,
          color: Colors.black,
          size: 24.0, // Adjust size as needed
        ),
      ),
    );
  }

  Widget _buildGreetingCard(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: const Color(0xffb219f0),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      padding: const EdgeInsets.all(16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Hello, Are you\nFeeling Good Today!',
                  style: context.displayMedium?.copyWith(
                    fontSize: 18,
                    color: Colors.white,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  'How Can I help You?',
                  style: context.displaySmall?.copyWith(
                    fontWeight: FontWeight.w300,
                    fontSize: 14,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 16),
                _buildStartChatButton(),
              ],
            ),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Image.asset(
              'assets/images/h_robot.png',
              fit: BoxFit.contain,
              height: context.height * 0.2,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStartChatButton() {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      padding: const EdgeInsets.all(8),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            'Start a Chat',
            style: const TextStyle(color: Color(0xffb219f0)),
          ),
          const SizedBox(width: 8),
          HugeIcon(
            icon: HugeIcons.strokeRoundedArrowAllDirection,
            color: const Color(0xffb219f0),
            size: 24.0, // Adjust size as needed
          ),
        ],
      ),
    );
  }

  Widget _buildFeaturedList() {
    return SizedBox(
      height: 40,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        itemCount: 5,
        itemBuilder: (context, index) {
          return Padding(
            padding: const EdgeInsets.symmetric(horizontal: 4.0),
            child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: Colors.grey.withOpacity(0.3)),
              ),
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              child: Center(
                child: Text(
                  'Featured',
                  style: context.displaySmall?.copyWith(
                    fontWeight: FontWeight.w400,
                    color: Colors.black,
                  ),
                ),
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildFeatureSection(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            _buildFeatureItem(
              'Text Summarizer',
              'Summarize long texts quickly',
              HugeIcons.strokeRoundedSummationCircle,
              const Color(0xfff3ba26),
              '/summarize-text',
              context,
            ),
            _buildFeatureItem(
              'Text to Image',
              'Generate images from text descriptions',
              HugeIcons.strokeRoundedAiImage,
              const Color(0xff7752fe),
              '/generate-image',
              context,
            ),
          ],
        ),
        const SizedBox(height: 16),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            _buildFeatureItem(
              'Signage Templates',
              'Create custom signage designs',
              HugeIcons.strokeRoundedDrawingCompass,
              const Color(0xff435585),
              '/signage-templates',
              context,
            ),
            _buildFeatureItem(
              'Blog Post Generator',
              'Write blog posts automatically',
              HugeIcons.strokeRoundedDrawingCompass,
              const Color(0xffe24d3b),
              '/generate-blog',
              context,
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildFeatureItem(
      String title,
      String description,
      IconData icon,
      Color color,
      String route,
      BuildContext context,
      ) {
    return GestureDetector(
      onTap: () {
        Navigator.pushNamed(context, route);
      },
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 300),
        width: MediaQuery.of(context).size.width * 0.43,
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(12),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.2),
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            HugeIcon(
              icon: icon,
              color: Colors.white,
              size: 40.0, // Adjust size as needed
            ),
            const SizedBox(height: 8),
            Text(
              title,
              style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.white),
            ),
            const SizedBox(height: 8),
            Text(
              description,
              style: const TextStyle(fontSize: 12, color: Colors.white),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAdditionalFeatures(BuildContext context) {
    return AnimatedContainer(
      duration: const Duration(milliseconds: 300),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          _buildFeatureItem(
            'Ads Copy Generator',
            'Create Ads Copy for Amazon Ads seamlessly',
            HugeIcons.strokeRoundedAddCircle,
            const Color(0xffb219f0),
            '/ads-to-keywords',
            context,
          ),
          _buildFeatureItem(
            'File to Text Extraction',
            'Extract text from various file formats',
            HugeIcons.strokeRoundedFileAdd,
            const Color(0xffb219f0),
            '/upload-file',
            context,
          ),
        ],
      ),
    );
  }
}
