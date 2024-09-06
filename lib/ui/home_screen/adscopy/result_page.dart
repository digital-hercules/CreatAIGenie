import 'package:flutter/material.dart';
import 'package:flutter/services.dart'; // For clipboard functionality

class ResultPage extends StatelessWidget {
  final List<String> keywords;

  ResultPage({required this.keywords});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Keywords Extracted',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
        ),
        backgroundColor: Color(0xFFB73C92), // Glossy purple color
        elevation: 8.0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: GridView.builder(
          gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2, // Number of columns in the grid
            crossAxisSpacing: 12.0, // Increased spacing for a spacious feel
            mainAxisSpacing: 12.0, // Increased spacing for a spacious feel
            childAspectRatio: 2.5, // Adjusted aspect ratio for more space
          ),
          itemCount: keywords.length,
          itemBuilder: (context, index) {
            return _buildKeywordButton(keywords[index], context);
          },
        ),
      ),
    );
  }

  Widget _buildKeywordButton(String keyword, BuildContext context) {
    return Center(
      child: SimpleElevatedButton(
        color: Color(0xFFB73C92).withOpacity(0.2), // Lighter shade for glossiness
        onPressed: () {
          _copyToClipboard(keyword, context);
        },
        child: Text(
          keyword,
          style: TextStyle(
            fontSize: 16, // Slightly larger font size for better readability
            color: Color(0xFFB73C92), // Matching text color
          ),
          textAlign: TextAlign.center,
        ),
        padding: EdgeInsets.symmetric(vertical: 12, horizontal: 16), // Adjusted padding
        borderRadius: 16, // More rounded corners for a modern look
      ),
    );
  }

  void _copyToClipboard(String text, BuildContext context) {
    Clipboard.setData(ClipboardData(text: text));
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Copied "$text" to clipboard!'),
        duration: Duration(seconds: 2),
        backgroundColor: Colors.black87, // Dark background for better readability
        behavior: SnackBarBehavior.floating, // Floating SnackBar for a modern look
      ),
    );
  }
}

class SimpleElevatedButton extends StatelessWidget {
  const SimpleElevatedButton({
    this.child,
    this.color,
    this.onPressed,
    this.borderRadius = 12,
    this.padding = const EdgeInsets.symmetric(horizontal: 28, vertical: 20),
    Key? key,
  }) : super(key: key);

  final Color? color;
  final Widget? child;
  final VoidCallback? onPressed;
  final double borderRadius;
  final EdgeInsetsGeometry padding;

  @override
  Widget build(BuildContext context) {
    ThemeData currentTheme = Theme.of(context);
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        padding: padding,
        backgroundColor: color ?? currentTheme.primaryColor, // Corrected parameter name
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(borderRadius),
        ),
        elevation: 8, // Added elevation for a glossy effect
        shadowColor: Colors.black.withOpacity(0.2), // Soft shadow for depth
      ),
      onPressed: onPressed,
      child: child,
    );
  }
}
