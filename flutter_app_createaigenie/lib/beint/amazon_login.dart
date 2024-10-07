import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';
import 'package:http/http.dart' as http;

class AmazonLogin {
  final String clientId;
  final String clientSecret;
  final String redirectUri;
  final String authorizationEndpoint = "https://www.amazon.com/ap/oa";
  final String tokenEndpoint = "https://api.amazon.com/auth/o2/token";

  AmazonLogin({
    required this.clientId,
    required this.clientSecret,
    required this.redirectUri,
  });

  Future<void> signIn(BuildContext context, VoidCallback onSuccess) async {
    try {
      final authUrl =
          "$authorizationEndpoint?client_id=$clientId&scope=profile&response_type=code&redirect_uri=$redirectUri";

      // Start the web authentication
      final result = await FlutterWebAuth.authenticate(
        url: authUrl,
        callbackUrlScheme: Uri.parse(redirectUri).scheme,
      );

      // Extract the authorization code from the redirect URI
      final code = Uri.parse(result).queryParameters['code'];

      // Request for access token using the authorization code
      final tokenResponse = await http.post(
        Uri.parse(tokenEndpoint),
        body: {
          'grant_type': 'authorization_code',
          'code': code,
          'client_id': clientId,
          'client_secret': clientSecret,
          'redirect_uri': redirectUri,
        },
      );

      // Parse the response to get the access token
      if (tokenResponse.statusCode == 200) {
        final tokenData = json.decode(tokenResponse.body);
        final accessToken = tokenData['access_token'];

        // Call the onSuccess callback
        onSuccess();

        // Show success message or navigate to home page
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Amazon Login successful! Access Token: $accessToken')),
        );
      } else {
        throw Exception('Failed to retrieve access token');
      }
    } catch (error) {
      // Handle login error
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Amazon Login failed: $error')),
      );
    }
  }
}
