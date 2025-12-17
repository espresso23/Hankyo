# 🇰🇷 Hankyo - Korean Learning Platform

![Java](https://img.shields.io/badge/Java-83.5%25-orange)
![CSS](https://img.shields.io/badge/CSS-8.5%25-blue)
![JavaScript](https://img.shields.io/badge/JavaScript-6.6%25-yellow)
![HTML](https://img.shields.io/badge/HTML-1.4%25-red)

**Hankyo** is a comprehensive online Korean language learning platform designed to help Vietnamese learners master Korean from beginner to advanced levels, including TOPIK exam preparation.

## 📖 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Database](#-database)
- [Deployment](#-deployment)
- [Key Functionalities](#-key-functionalities)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 🎓 Core Learning Features
- **Video Lectures**: High-quality 4K video lessons with detailed grammar explanations
- **Flashcard Games**: Interactive vocabulary learning through memory games and flashcard systems
- **AI-Powered Learning**: Integration with Google Gemini AI for:
  - Korean sentence correction and feedback
  - Exercise analysis and hints
  - Vietnamese-Korean dictionary with examples
  - Personalized learning assistance

### 📚 Course Management
- **Multi-Level Learning Paths**:
  - Beginner (Sơ cấp)
  - Intermediate (Trung cấp)
  - TOPIK Exam Preparation
- **Comprehensive Course Catalog**: Organized by categories and skill levels
- **Progress Tracking**: Monitor your learning journey with Hankyo Points

### 🎮 Interactive Learning
- **Memory Matching Game**: Match Korean words with Vietnamese meanings
- **Vocabulary Practice**: Extensive word banks covering various topics (food, daily life, grammar)
- **Gamification**: Earn points, honors, and rewards as you progress

### 💎 Premium Features
- **VIP Membership System**: Monthly and yearly subscription plans
- **Exclusive Content**: Access to premium courses and materials
- **Priority Support**: Enhanced learning experience for VIP members

### 👥 User Management
- **Learner Profiles**: Personalized accounts with avatar support
- **Social Authentication**: Easy login and registration
- **Role-Based Access**: Different permissions for learners, instructors, and administrators

### 💳 Payment Integration
- **PayOS Integration**: Secure payment processing for course purchases and subscriptions
- **Multiple Payment Methods**: Flexible payment options for Vietnamese users

## 🛠 Technology Stack

### Backend
- **Language**: Java 17
- **Framework**: Java Servlets & JSP
- **Build Tool**: Maven
- **Database**: Microsoft SQL Server 2019
- **Connection Pool**: HikariCP

### Frontend
- **HTML5 & CSS3**: Responsive design with custom styling
- **JavaScript**: Interactive UI components
- **Bootstrap 5.1.3**: Modern, mobile-first framework
- **JSTL**: JavaServer Pages Standard Tag Library

### AI & External Services
- **Google Cloud AI Platform**: Gemini AI integration for intelligent tutoring
- **Cloudinary**: Image and media management
- **PayOS**: Payment gateway for Vietnam

### DevOps & Deployment
- **Docker**: Containerized application
- **Docker Compose**: Multi-container orchestration
- **Web Server**: Servlet Container (Tomcat/Jetty compatible)

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: Version 3.6+
- **Docker & Docker Compose**: For containerized deployment
- **SQL Server**: 2019 or compatible version

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/espresso23/Hankyo.git
   cd Hankyo
   ```

2. **Configure Database**
   - Import the SQL schema from `hankyo.sql`
   - Update database credentials in your configuration

3. **Build with Maven**
   ```bash
   mvn clean install
   ```

4. **Run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

5. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`

### Manual Deployment

If you prefer to run without Docker:

1. **Deploy the WAR file**
   ```bash
   mvn clean package
   # Deploy Hankyo.war to your servlet container
   ```

2. **Configure Database Connection**
   - Set up SQL Server connection parameters
   - Update connection strings in your environment

3. **Start the application server**
   ```bash
   # For Tomcat
   catalina.sh run
   ```

## 📁 Project Structure

```
Hankyo/
├── src/
│   └── main/
│       ├── java/
│       │   ├── model/          # Data models (Learner, Course, Reward, etc.)
│       │   ├── service/        # Business logic (GeminiService, Payment, etc.)
│       │   ├── controller/     # Servlet controllers
│       │   └── dao/            # Data Access Objects
│       └── webapp/
│           ├── home.jsp        # Landing page
│           ├── courses.jsp     # Course catalog
│           ├── memory-game.jsp # Vocabulary game
│           ├── bundles.jsp     # VIP subscription plans
│           ├── about-us.jsp    # About page
│           ├── recruitment.jsp # Career page
│           ├── term.jsp        # Terms of service
│           └── asset/          # Static resources (CSS, JS, images)
├── WebContent/                 # Additional web resources
├── pom.xml                     # Maven configuration
├── docker-compose.yml          # Docker services configuration
├── Dockerfile                  # Container build instructions
├── hankyo.sql                  # Database schema
└── banks.json                  # Banking information for payments
```

## 🗄 Database

The platform uses **Microsoft SQL Server** with the following key tables:

- **Users & Authentication**: User accounts, roles, and authentication
- **Courses & Lessons**: Course catalog, video lessons, and learning materials
- **Vocabulary & Flashcards**: Word banks for language practice
- **Progress Tracking**: Learner achievements, points, and progress
- **Payments & Subscriptions**: VIP memberships and transaction records
- **Rewards & Honors**: Gamification elements

Import the schema using:
```sql
sqlcmd -S localhost -U sa -P YourPassword -i hankyo.sql
```

## 🐳 Deployment

### Docker Deployment

The application includes Docker configuration for easy deployment:

```yaml
services:
  app:
    - Java application on port 8080
    - Connected to SQL Server database
  
  db:
    - SQL Server 2019 Express
    - Persistent volume for data storage
```

### Environment Variables

Configure the following environment variables:

- `DB_HOST`: Database server hostname
- `DB_USER`: Database username
- `DB_PASS`: Database password
- `DB_NAME`: Database name (default: hankyo)

## 🎯 Key Functionalities

### 1. AI-Powered Korean Learning
The platform integrates Google Gemini AI to provide:
- Real-time Korean sentence correction
- Contextual grammar explanations
- Personalized exercise hints
- Intelligent Vietnamese-Korean translation with examples

### 2. Gamified Learning Experience
- **Hankyo Points**: Earn points for completing lessons and exercises
- **Honor System**: Achieve various honor badges
- **Rewards**: Unlock special features and content
- **Leaderboards**: Compete with other learners

### 3. Comprehensive Vocabulary System
- 1000+ Korean words with Vietnamese translations
- Organized by topics (food, daily life, grammar patterns)
- Interactive flashcard games
- Memory matching exercises

### 4. Flexible Subscription Model
- **Free Tier**: Access to basic courses and features
- **Monthly VIP**: Premium content with monthly billing
- **Yearly VIP**: Cost-effective annual subscription
- **Secure Payments**: PayOS integration for Vietnamese payment methods

### 5. Progress Tracking
- Personal learning dashboard
- Course completion tracking
- Skill level assessment
- TOPIK exam preparation monitoring

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Write meaningful commit messages
- Test your changes thoroughly
- Update documentation as needed

## 📝 License

This project is currently unlicensed. Please contact the repository owner for usage permissions.

## 📞 Contact & Support

- **Platform**: [Hankyo Learning Platform](https://github.com/espresso23/Hankyo)
- **Developer**: [@espresso23](https://github.com/espresso23)
- **Issues**: [GitHub Issues](https://github.com/espresso23/Hankyo/issues)

## 🌟 Why Choose Hankyo?

> "Tại HANKYO, chúng tôi tin rằng dù bạn ở bất cứ nơi đâu cũng đều cần được hưởng chất lượng học tập tốt nhất."

- 🎥 **High-Quality Video Lessons**: 4K videos with unlimited replay
- 🎮 **Fun Learning Games**: Vocabulary practice through interactive games
- 🤖 **AI Assistant**: Powered by Google Gemini for personalized support
- 📱 **Learn Anywhere**: Responsive design for all devices
- 🏆 **10,000+ Learners**: Join our growing community across Vietnam
- 👨‍🏫 **Expert Instructors**: Learn from experienced Korean language teachers

---

**Made with ❤️ for Vietnamese learners aspiring to master Korean**

*Start your Korean learning journey today with Hankyo!* 🇰🇷✨