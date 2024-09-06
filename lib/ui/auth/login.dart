import 'package:aigeniog/ui/common_widgets/round_button.dart';
import 'package:aigeniog/ui/user_selection/user_selection.dart';
import 'package:aigeniog/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../beint/api_service.dart';
import '../../utils/constants/app_spacer_constants.dart';
import '../../utils/constants/scale_factor.dart';
import '../home_screen/home.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final ApiService _apiService = ApiService();
  final GlobalKey<FormState> _formkey = GlobalKey<FormState>();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController emailController = TextEditingController();

  bool _loading = false;
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
          textScaler: TextScaler.linear(
            ScaleSize.textScaleFactor(context),
          ),
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
          key: _formkey,
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
        voidCallBack: _loading ? () {} : () => _login(),
      ),
      AppSpacer.p10(),
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            'Do not have an account ?',
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
                MaterialPageRoute(
                  builder: (context) => const UserSelection(),
                ),
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

Future<void> _login() async {
  if (_formkey.currentState!.validate()) {
    setState(() {
      _loading = true;
    });

    final email = emailController.text;
    final password = passwordController.text;
    final token = await _apiService.login(email, password);

    setState(() {
      _loading = false;
    });

    if (token != null) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const Home()),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Login failed. Please try again.')),
      );
    }
  }
}
  
}