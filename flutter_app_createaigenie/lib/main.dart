import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

// UI Screens
import 'package:aigeniog/ui/features/page_view.dart';
import './ui/home_screen/home_screen.dart';
import './ui/auth/login.dart';
import './ui/home_screen/adscopy/create_ads.dart';

// Utils and Shared Work
import 'package:aigeniog/utils/theme/themes.dart';
import 'utils/sharedwork/summarize_text_screen.dart';
import 'utils/sharedwork/generate_image_screen.dart';
import 'utils/sharedwork/signage_templates_screen.dart';
import 'utils/sharedwork/signage_template_detail_screen.dart';
import 'utils/sharedwork/file_upload_screen.dart';
import 'utils/sharedwork/text_to_speech_screen.dart';

// Error logging package (optional but recommended for production apps)
import 'package:logger/logger.dart';

// Initialize the logger
final logger = Logger();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );
    runApp(const MyApp());
  } catch (e) {
    // Log the error and show an error screen
    logger.e("Error initializing Firebase: $e");
    runApp(ErrorScreen(error: e.toString()));
  }
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: const Size(360, 800),
      minTextAdapt: true,
      splitScreenMode: true,
      useInheritedMediaQuery: true,
      builder: (_, child) {
        return MaterialApp(
          title: 'AI Operations',
          themeMode: ThemeMode.system,
          theme: TAppTheme.lightTheme,
          darkTheme: TAppTheme.darkTheme, // Assuming you have defined TAppTheme.darkTheme
          initialRoute: Routes.login,
          routes: {
            Routes.login: (context) => const Login(),
            Routes.home: (context) => const HomeScreen(),
            Routes.summarizeText: (context) => SummarizeTextScreen(),
            Routes.generateImage: (context) => GenerateImageScreen(),
            Routes.signageTemplates: (context) => SignageTemplatesScreen(),
            Routes.signageDetail: (context) => SignageTemplateDetailScreen(),
            Routes.adsToKeywords: (context) => CreateAds(),
            Routes.uploadFile: (context) => FileUploadScreen(),
            Routes.textToSpeech: (context) => TextToSpeechScreen(),
            Routes.pageView: (context) => const PageViewFlutter(),
          },
        );
      },
    );
  }
}

class ErrorScreen extends StatelessWidget {
  final String error;

  const ErrorScreen({Key? key, required this.error}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Initialization Error')),
        body: Center(
          child: Text('Failed to initialize Firebase: $error'),
        ),
      ),
    );
  }
}

class Routes {
  static const login = '/login';
  static const home = '/home';
  static const summarizeText = '/summarize-text';
  static const generateImage = '/generate-image';
  static const signageTemplates = '/signage-templates';
  static const signageDetail = '/signage-detail';
  static const adsToKeywords = '/ads-to-keywords';
  static const uploadFile = '/upload-file';
  static const textToSpeech = '/text-to-speech';
  static const pageView = '/page-view';
}
