<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Danh sách ngân hàng</title>
</head>
<body>
<form action="submitBankDetails" method="POST">
    <div class="form-group">
        <label for="bankAccount">Tài khoản ngân hàng</label>
        <input type="text" id="bankAccount" name="bankAccount" class="form-control" placeholder="Nhập số tài khoản"
               required>
    </div>

    <div class="form-group">
        <label for="bank">Chọn ngân hàng</label>
        <select id="bank" name="bank" class="form-control" required>
            <option value="">-- Chọn ngân hàng --</option>
            <c:forEach var="bank" items="${bankList}">
                <option value="${bank.code},${bank.bin}">${bank.name}</option>
            </c:forEach>
        </select>
    </div>

    <button type="submit" class="btn btn-primary">Xác nhận</button>
</form>

</body>
</html>
