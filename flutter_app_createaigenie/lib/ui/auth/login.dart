
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:shared_preferences/shared_preferences.dart'; // Import SharedPreferences
import 'package:create_ai_genie/ui/common_widgets/round_button.dart';
import 'package:create_ai_genie/ui/user_selection/user_selection.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import '../../utils/constants/app_spacer_constants.dart';
import '../../utils/constants/scale_factor.dart';
import '../home_screen/home.dart';
import '../../beint/amazon_login.dart';
import 'package:tutorial_coach_mark/tutorial_coach_mark.dart';

import 'package:http/http.dart' as http;

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  bool _loading = false;
  bool _isObscure = true;

  // Initialize AmazonLogin instance
  final AmazonLogin amazonLogin = AmazonLogin(
    clientId: 'YOUR_CLIENT_ID', // Replace with your Client ID
    clientSecret: 'YOUR_CLIENT_SECRET', // Replace with your Client Secret
    redirectUri: 'https://creataigenie.com/auth', // Replace with your Redirect URI
  );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: () => Navigator.pop(context),
          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: CustomScrollView(
          slivers: [
            SliverFillRemaining(
              hasScrollBody: false,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  _buildLoginForm(context),
                  _buildLoginButton(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildLoginForm(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        const SizedBox(width: double.infinity),
        Text(
          'Create AI Genie',
          style: context.displayMedium,
          textScaler: TextScaler.linear(ScaleSize.textScaleFactor(context)),
          maxLines: 1,
        ),
        AppSpacer.p10(),
        Text(
          'Login',
          style: context.displayLarge,
        ),
        SizedBox(
          height: 90,
          child: Image.asset('assets/icons/login.png'),
        ),
        Text(
          'Enter the details',
          style: context.displaySmall!.copyWith(color: Colors.black),
        ),
        Form(
          key: _formKey,
          child: Column(
            children: [
              const SizedBox(height: 20),
              _buildEmailField(),
              const SizedBox(height: 20),
              _buildPasswordField(),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildEmailField() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Email Id',
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),
        ),
        TextFormField(
          controller: emailController,
          decoration: const InputDecoration(
            hintText: 'Enter your email id',
            enabled: true,
          ),
          validator: (value) {
            if (value == null || value.isEmpty) {
              return "Email cannot be empty";
            }
            if (!RegExp(r"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\.[a-z]+").hasMatch(value)) {
              return "Please enter a valid email";
            }
            return null;
          },
          keyboardType: TextInputType.emailAddress,
        ),
      ],
    );
  }

  Widget _buildPasswordField() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Password',
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),
        ),
        TextFormField(
          obscureText: _isObscure,
          controller: passwordController,
          decoration: InputDecoration(
            suffixIcon: IconButton(
              icon: Icon(_isObscure ? Icons.visibility_off : Icons.visibility),
              onPressed: () {
                setState(() {
                  _isObscure = !_isObscure;
                });
              },
            ),
            hintText: 'Enter your password',
          ),
          validator: (value) {
            if (value == null || value.isEmpty) {
              return "Password cannot be empty";
            }
            if (value.length < 6) {
              return "Please enter a valid password (min. 6 characters)";
            }
            return null;
          },
        ),
      ],
    );
  }

  Widget _buildLoginButton() {
    return Column(
      children: [
        RoundButton(
          text: _loading ? 'Logging in...' : 'Login',
          voidCallBack: _loading ? () {} : () => _login(emailController.text, passwordController.text),
        ),
        AppSpacer.p10(),
        _buildAmazonLoginButton(),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Do not have an account?',
              style: GoogleFonts.poppins(
                textStyle: const TextStyle(
                  fontWeight: FontWeight.w400,
                  fontSize: 14.0,
                  height: 1.25,
                  letterSpacing: -0.01,
                  color: Colors.black,
                ),
              ),
            ),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const UserSelection()),
                );
              },
              child: const Text(
                'Sign Up',
                style: TextStyle(color: Color(0xffB219F0)),
              ),
            ),
          ],
        ),
        const SizedBox(height: 20),
      ],
    );
  }

  Widget _buildAmazonLoginButton() {
    return Container(
      width: double.infinity,
      height: 50,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(30),
        gradient: const LinearGradient(
          colors: [Color(0xFFFF9900), Color(0xFFF7981C)],
          begin: Alignment.centerLeft,
          end: Alignment.centerRight,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            spreadRadius: 1,
            blurRadius: 10,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 15, horizontal: 10),
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
        ),
        onPressed: _amazonLogin,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: const [
            Text(
              'Login with Amazon',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 16,
                color: Colors.white,
                letterSpacing: 1.2,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _login(String email, String password) async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _loading = true;
      });

      try {
        final response = await http.post(
          Uri.parse('http://48.216.211.10:8000/auth/jwt/create/'), // Your API endpoint
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(<String, String>{
            'email': email,
            'password': password,
          }),
        );

        print('Response status: ${response.statusCode}');
        print('Response body: ${response.body}');

        setState(() {
          _loading = false;
        });

        if (response.statusCode == 200) {
          final responseData = jsonDecode(response.body);

          // Retrieve both tokens from the response
          final String refreshToken = responseData['refresh'];
          final String accessToken = responseData['access'];

          // Debug print to confirm the tokens
          print('Access Token: $accessToken');
          print('Refresh Token: $refreshToken');

          // Check if tokens are null before saving
          if (accessToken != false && refreshToken != false) {
            final prefs = await SharedPreferences.getInstance();
            await prefs.setString('accessToken', accessToken);
            await prefs.setString('refreshToken', refreshToken);

            // Verify that tokens are saved
            print('Tokens saved successfully');
          } else {
            print('Failed to retrieve tokens from response');
            _showErrorMessage('Login failed. Please check your credentials.');
            return;
          }

          // Redirect to Home after successful login
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => const Home()),
          );
        } else {
          _showErrorMessage('Login failed. Please check your credentials.');
        }
      } catch (e) {
        setState(() {
          _loading = false;
        });
        print('Error during login: $e');
        _showErrorMessage('An error occurred. Please try again later.');
      }
    }
  }

  Future<void> _amazonLogin() async {
    await amazonLogin.signIn(context, () {
      // Redirect to Home on successful Amazon login
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const Home()),
      );
    });
  }

  void _showErrorMessage(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
      ),
    );
  }
}  