#!/bin/bash

# Vinyleaf Release Setup Script
# This script helps you set up everything needed for GitHub releases

echo "🎵 Vinyleaf Release Setup"
echo "========================"

# Create keystore directory
mkdir -p keystore

echo "📱 Creating release keystore..."
echo "Please provide the following information for your app signing key:"
read -p "Enter your name: " dev_name
read -p "Enter your organization: " organization
read -p "Enter your country code (e.g., US, IN): " country

# Generate keystore
keytool -genkey -v \
    -keystore keystore/release-keystore.jks \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -alias vinyleaf \
    -dname "CN=$dev_name, O=$organization, C=$country"

echo ""
echo "✅ Keystore created successfully!"
echo ""
echo "🔐 Next steps for GitHub Actions:"
echo "1. Go to your GitHub repository settings"
echo "2. Navigate to Secrets and Variables > Actions"
echo "3. Add these secrets:"
echo "   - SIGNING_KEY: (base64 encoded keystore file)"
echo "   - ALIAS: vinyleaf"
echo "   - KEY_STORE_PASSWORD: (the password you just entered)"
echo "   - KEY_PASSWORD: (the password you just entered)"
echo ""
echo "💡 To get the base64 encoded keystore:"
echo "   Linux/Mac: base64 -i keystore/release-keystore.jks"
echo "   Windows: certutil -encode keystore/release-keystore.jks keystore.txt"
echo ""
echo "🚀 After setting up secrets, create a release by:"
echo "   git tag v1.0.0"
echo "   git push origin v1.0.0"
echo ""
echo "⚠️  IMPORTANT: Never commit the keystore file to git!"
echo "   It's already excluded in .gitignore"
