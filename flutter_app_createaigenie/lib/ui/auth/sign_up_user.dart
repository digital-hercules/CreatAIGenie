import 'dart:convert'; // For JSON encoding and decoding
import 'package:create_ai_genie/ui/common_widgets/round_button.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;  // Updated to http for API calls
import 'package:shared_preferences/shared_preferences.dart';  // For saving token (if needed)
import '../../utils/constants/app_spacer_constants.dart';
import '../../utils/constants/scale_factor.dart';
import '../home_screen/home.dart';
import 'login.dart';

// Define your API base URL here
const String baseUrl = ''; // Replace with actual base URL

class SignUpUser extends StatefulWidget {
  const SignUpUser({super.key});

  @override
  State<SignUpUser> createState() => _SignUpUserState();
}

class _SignUpUserState extends State<SignUpUser> {
  bool showProgress = false;
  final _formkey = GlobalKey<FormState>();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmpassController = TextEditingController();
  final TextEditingController name = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  bool _isObscure = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: () {
            Navigator.pop(context);
          },
          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(left: 20, right: 20),
        child: CustomScrollView(
          slivers: [
            SliverFillRemaining(
              hasScrollBody: false,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  _buildSignUpForm(context),
                  _buildSignUpButton(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSignUpForm(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        const SizedBox(width: double.infinity),
        Text(
          'Create AI Genie',
          style: context.displayMedium,  // Your custom style
          textScaler: TextScaler.linear(ScaleSize.textScaleFactor(context)),
          maxLines: 1,
        ),
        AppSpacer.p10(),
        Text(
          'Please fill in the details!',
          style: context.displayLarge,  // Your custom style
        ),
        Stack(
          children: [
            SizedBox(height: 90, child: Image.asset('assets/icons/sign_person.png')),
            Positioned(
              right: 0,
              bottom: -1,
              child: Image.asset('assets/icons/sign_add.png'),
            ),
          ],
        ),
        Form(
          key: _formkey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildNameField(),
              const SizedBox(height: 20),
              _buildEmailField(),
              const SizedBox(height: 20),
              _buildPhoneNumberField(),
              const SizedBox(height: 20),
              _buildPasswordField(),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildNameField() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Full Name',
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)), // Your custom style
        ),
        TextFormField(
          controller: name,
          decoration: InputDecoration(
            hintText: 'Enter your Name',
            hintStyle: const TextStyle(color: Color(0xffBABABA)),
            enabled: true,
            contentPadding: const EdgeInsets.only(left: 14.0, bottom: 8.0, top: 8.0),
            focusedBorder: OutlineInputBorder(
              borderSide: const BorderSide(color: Color(0xffCBD2E0)),
              borderRadius: BorderRadius.circular(8),
            ),
            enabledBorder: OutlineInputBorder(
              borderSide: const BorderSide(color: Color(0xffCBD2E0)),
              borderRadius: BorderRadius.circular(8),
            ),
          ),
          validator: (value) {
            if (value!.isEmpty) {
              return "Name cannot be empty";
            }
            if (!RegExp(r'^[a-zA-Z]+$').hasMatch(value)) {
              return "Please enter a valid name (only letters)";
            }
            return null;
          },
          keyboardType: TextInputType.name,
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
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),  // Your custom style
        ),
        TextFormField(
          controller: emailController,
          decoration: const InputDecoration(
            hintText: 'Enter your email id',
            enabled: true,
          ),
          validator: (value) {
            if (value!.isEmpty) {
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

  Widget _buildPhoneNumberField() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Phone Number',
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),  // Your custom style
        ),
        TextFormField(
          controller: mobileController,
          decoration: const InputDecoration(hintText: 'Enter your phone number'),
          validator: (value) {
            if (value!.isEmpty) {
              return "Number cannot be empty";
            }
            if (!RegExp(r'^[0-9]+$').hasMatch(value)) {
              return "Please enter a valid phone number";
            }
            return null;
          },
          keyboardType: TextInputType.phone,
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
          style: context.displaySmall!.copyWith(color: const Color(0xff666666)),  // Your custom style
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
            hintText: 'Enter a strong password',
          ),
          validator: (value) {
            RegExp regex = RegExp(r'^.{6,}$');
            if (value!.isEmpty) {
              return "Password cannot be empty";
            }
            if (!regex.hasMatch(value)) {
              return "Please enter a valid password (min. 6 characters)";
            }
            return null;
          },
        ),
      ],
    );
  }

  Widget _buildSignUpButton() {
    return Column(
      children: [
        RoundButton(
          text: showProgress ? 'Signing Up...' : 'Sign Up',
          voidCallBack: showProgress ? () {} : _signUp, // Prevent double tap
        ),
        AppSpacer.p10(),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Already have an account?',
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
                Navigator.push(context, MaterialPageRoute(builder: (context) => const Login()));
              },
              child: const Text('Log In', style: TextStyle(color: Color(0xffB219F0))),
            ),
          ],
        ),
        const SizedBox(height: 20),
      ],
    );
  }

  Future<void> _signUp() async {
    if (_formkey.currentState!.validate()) {
      setState(() {
        showProgress = true;
      });

      // Commented out the API call to bypass sign-up logic
      /*
      final success = await signUp(
        emailController.text,
        passwordController.text,
        name.text,
        mobileController.text,
      );

      setState(() {
        showProgress = false;
      });

      if (success) {
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const Home()));
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Sign Up failed. Please try again.')),
        );
      }
      */

      // Automatically navigate to the Home page on form submission
      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const Home()));
    }
  }

  Future<bool> signUp(String email, String password, String name, String mobile) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/signup/'),  // Adjust this to your API endpoint
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'email': email,
          'password': password,
          'name': name,
          'mobile': mobile,
        }),
      );

      // Check if the response was successful (HTTP 200)
      if (response.statusCode == 201) { // Adjust for your successful status code
        // You can handle the token or other data if returned
        return true;
      } else {
        // Handle errors or unsuccessful response
        return false;
      }
    } catch (e) {
      // Handle network or other errors
      print('Error during sign up: $e');
      return false;
    }
  }
}
