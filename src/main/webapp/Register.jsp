<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign-Up</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Asset/Footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Asset/Register.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<header>
    <img class="logo" src="Asset/PNG/logo.png" alt="Logo">
    <div class="navbarContainer">
        <div class="navbarContent"><a href="index.html">Khóa Học</a></div>
        <div class="navbarContent"><a href="about.html">Giảng Viên</a></div>
        <div class="navbarContent"><a href="community.html">Cộng Đồng</a></div>
        <div class="navbarContent"><a href="docs.html">Tài Liệu</a></div>
        <div class="navbarContent"><a href="test.html">Thi Thử</a></div>
        <div class="navbarContent"><a href="about-us.html">Về Chúng Tôi</a></div>
    </div>
</header>
<body>
<div class="container-fluid">
            <div class="signUp-form">
                <h2>Register</h2>
                <div class="form-group">
                    <form method="POST" action="${pageContext.request.contextPath}/Register">
                        <div class="input">
                            <input type="text" id="fullName" name="fullName" placeholder="Enter your full name"
                                   required>
                        </div>
                        <div class="input">
                            <input type="text" id="username" name="username" placeholder="Username" required>
                        </div>
                        <div class="input">
                            <input type="email" id="gmail" name="gmail" placeholder="gmail" required>
                        </div>
                        <div class="input">
                            <input type="password" id="password" name="password" placeholder="Password" required>
                        </div>
                        <div class="input">
                            <input type="password" id="confirmPassword" name="confirmPassword"
                                   placeholder="Re-enter Password" required>
                        </div>
                        <div class="input">
                            <input type="tel" id="phone" name="phone" placeholder="Phone Number" required>
                        </div>
                        <div>
                        <h6>Gender:</h6>
                        <div class="gender-selection">
                            <div class="gender-labels">
                                <label for="male">Male</label>
                                <label for="female">Female</label>
                            </div>
                            <div class="gender-radios">
                                <input type="radio" id="male" name="gender" value="Male" required>
                                <input type="radio" id="female" name="gender" value="Female" required>
                            </div>
                        </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/Register?p=hadaccount" style="margin-left: 5px;">Already
                            have an account?</a>
                        <button type="submit">Sign Up</button>
                        <c:if test="${not empty msg}">
                            <div class="alert alert-danger">
                                <c:out value="${msg}"/>
                            </div>
                        </c:if>
                    </form>
                </div>

                </div>
            </div>
        </div>
    </div>
</div>


<div>
    <footer class="footer mt-auto py-4 bg-light border-top">
        <div class="container">
            <div class="row d-flex flex-wrap justify-content-between">
                <!-- Cột 1: Use Cases -->
                <div class="col-lg-3 col-md-6">
                    <img src="Asset/PNG/logo.png" alt="Logo" class="footer-logo img-fluid">
                </div>

                <div class="col-lg-3 col-md-6">
                    <h5 class="footer-heading">Use Cases</h5>
                    <ul class="list-unstyled">
                        <li><a href="#" class="footer-link">UI Design</a></li>
                        <li><a href="#" class="footer-link">UX Design</a></li>
                        <li><a href="#" class="footer-link">Wireframing</a></li>
                    </ul>
                </div>

                <!-- Cột 2: Explore -->
                <div class="col-lg-3 col-md-6">
                    <h5 class="footer-heading">Explore</h5>
                    <ul class="list-unstyled">
                        <li><a href="#" class="footer-link">Design</a></li>
                        <li><a href="#" class="footer-link">Prototyping</a></li>
                        <li><a href="#" class="footer-link">Development Features</a></li>
                    </ul>
                </div>

                <!-- Cột 3: Resources -->
                <div class="col-lg-3 col-md-6">
                    <h5 class="footer-heading">Resources</h5>
                    <ul class="list-unstyled">
                        <li><a href="#" class="footer-link">Blog</a></li>
                        <li><a href="#" class="footer-link">Best Practices</a></li>
                        <li><a href="#" class="footer-link">Colors</a></li>
                        <li><a href="#" class="footer-link">Color Wheel</a></li>
                        <li><a href="#" class="footer-link">Support</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </footer>

</div>
</body>
</html>