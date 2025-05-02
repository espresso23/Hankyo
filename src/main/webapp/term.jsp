<%--
  Created by IntelliJ IDEA.
  User: LAPTOP VINH HA
  Date: 5/1/2025
  Time: 10:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Điều Khoản Sử Dụng - HANKYO</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #0D6EFD;
            --secondary-color: #FF9F1C;
            --text-dark: #333;
            --text-light: #666;
            --bg-light: #f8f9fa;
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            line-height: 1.6;
            color: var(--text-dark);
            background-color: var(--bg-light);
        }

        .terms-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }

        .terms-header {
            text-align: center;
            margin-bottom: 3rem;
            padding-bottom: 2rem;
            border-bottom: 2px solid var(--primary-color);
        }

        .terms-header h1 {
            color: var(--primary-color);
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
        }

        .terms-section {
            margin-bottom: 2.5rem;
        }

        .terms-section h2 {
            color: var(--primary-color);
            font-size: 1.8rem;
            margin-bottom: 1.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #eee;
        }

        .terms-section h3 {
            color: var(--text-dark);
            font-size: 1.4rem;
            margin: 1.5rem 0 1rem;
        }

        .terms-section p, .terms-section li {
            color: var(--text-light);
            margin-bottom: 1rem;
            font-size: 1.1rem;
        }

        .terms-section ul {
            padding-left: 2rem;
            margin-bottom: 1.5rem;
        }

        .terms-section li {
            margin-bottom: 0.5rem;
        }

        .highlight {
            background-color: #fff3cd;
            padding: 1rem;
            border-left: 4px solid var(--secondary-color);
            margin: 1rem 0;
        }

        .payment-section {
            background-color: #e9ecef;
            padding: 2rem;
            border-radius: 8px;
            margin-top: 3rem;
        }

        .payment-section h2 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
        }

        .bank-details {
            background: white;
            padding: 1.5rem;
            border-radius: 8px;
            margin-top: 1rem;
        }

        .bank-details p {
            margin-bottom: 0.5rem;
        }

        .contact-info {
            margin-top: 2rem;
            padding: 1.5rem;
            background: #f8f9fa;
            border-radius: 8px;
        }

        @media (max-width: 768px) {
            .terms-container {
                margin: 1rem;
                padding: 1rem;
            }

            .terms-header h1 {
                font-size: 2rem;
            }

            .terms-section h2 {
                font-size: 1.5rem;
            }

            .terms-section h3 {
                font-size: 1.2rem;
            }
        }
    </style>
</head>
<body>
    <div class="terms-container">
        <div class="terms-header">
            <h1>ĐIỀU KHOẢN SỬ DỤNG</h1>
        </div>

        <div class="terms-section">
            <h2>Điều 1: Nguyên tắc đăng ký Tài khoản Hankyo</h2>
            <ul>
                <li>Bạn phải chịu trách nhiệm hoàn toàn trước mọi thông tin đăng ký tài khoản Hankyo, thông tin sửa đổi, bổ sung tài khoản Hankyo</li>
                <li>Ngoài việc tuân thủ Thỏa thuận sử dụng, bạn phải chấp hành nghiêm chỉnh các thỏa thuận, quy định, quy trình khác của Hankyo được đăng tải công khai trên website Hankyo.</li>
                <li>Bạn không được đặt tài khoản theo tên của danh nhân, tên các vị lãnh đạo của Đảng và Nhà nước, tên của cá nhân, tổ chức tội phạm, phản động, khủng bố hoặc tài khoản có ý nghĩa không lành mạnh, trái với thuần phong mỹ tục.</li>
            </ul>
        </div>

        <div class="terms-section">
            <h2>Điều 2: Đăng ký và ngừng sử dụng dịch vụ</h2>
            <ul>
                <li>Trước khi đăng ký tài khoản, bạn xác nhận đã đọc, hiểu và đồng ý với tất cả các quy định trong Thỏa thuận sử dụng của Hankyo</li>
                <li>Khi đăng ký tài khoản bạn phải đồng ý cung cấp thông tin một cách trung thực, đầy đủ và cập nhật các thông tin này khi có sự thay đổi trong thực tế. Các vấn đề thu thập, sử dụng và bảo mât thông tin vui lòng xem trong chính sách bảo mật.</li>
                <li>Khi đăng ký, bạn sẽ tạo một tài khoản Hankyo với tên truy cập và một mật khẩu. Bạn đồng ý chịu trách nhiệm bảo vệ tên truy cập và mật khẩu của mình để tránh việc người khác sử dụng trái phép và thông báo kịp thời cho Hankyo về bất kỳ việc sử dụng trái phép nào.</li>
                <li>Bạn có thể chấm dứt việc đăng ký nếu bạn không muốn tiếp tục sử dụng dịch vụ của Hankyo. Hankyo có thể chấm dứt việc đăng ký của bạn hoặc cấm bạn truy cập vào một số phần của dịch vụ nếu có căn cứ xác định bạn đã vi phạm thỏa thuận.</li>
            </ul>
        </div>

        <div class="terms-section">
            <h2>Điều 3: Các nội dung cấm trao đổi, chia sẻ trên mạng xã hội</h2>
            <ol>
                <li>
                    <strong>Chống phá nhà nước, kích động bạo lực và lan truyền thông tin sai lệch:</strong>
                    <ul>
                        <li>Hoàn toàn cấm tuyên truyền, phá hoại hoặc biến tấu thông tin về chính phủ, cơ cấu chính trị và chính sách quốc gia.</li>
                        <li>Cấm kích động bạo lực, thúc đẩy chiến tranh, gây căm ghét và khuyến khích hành vi đồi trụy, tội ác.</li>
                        <li>Không được phổ biến thông tin mê tín, quan niệm sai lầm hoặc phá hoại truyền thống văn hóa.</li>
                        <li>Cấm đăng tải bài viết từ các trang nước ngoài như BBC tiếng Việt, VOA tiếng Việt...</li>
                        <li>Cấm kêu gọi tổ chức bạo loạn và đăng tải thông tin về biểu tình, bạo loạn.</li>
                    </ul>
                </li>
                <li>
                    <strong>Gây tổn hại đến uy tín sản phẩm, tổ chức cá cược:</strong>
                    <ul>
                        <li>Tuyệt đối không chấp nhận hành vi gây tổn hại đến uy tín sản phẩm của công ty phát hành.</li>
                        <li>Nghiêm cấm tổ chức các hình thức cá cược, cờ bạc hoặc các thỏa thuận liên quan đến tiền, hiện kim, hiện vật.</li>
                    </ul>
                </li>
                <li>
                    <strong>Xúc phạm, nhạo báng người khác, phá hoại cộng đồng:</strong>
                    <ul>
                        <li>Nghiêm cấm việc xúc phạm, nhạo báng người khác dưới bất kỳ hình thức nào.</li>
                        <li>Cấm các hành vi phá hoại và làm ảnh hưởng đến giá trị của cộng đồng.</li>
                    </ul>
                </li>
                <li>
                    <strong>Nội dung không phù hợp với thuần phong mỹ tục của Việt Nam và vi phạm pháp luật:</strong>
                    <ul>
                        <li>Nghiêm cấm đưa Đảng Cộng sản Việt Nam, Nhà nước Việt Nam, các thể chế chính trị, lịch sử, các lãnh tụ vào bàn luận trong những chủ đề không liên quan.</li>
                        <li>Cấm mọi hành vi mạo nhận hoặc cố ý mạo danh làm người khác để đánh lừa.</li>
                    </ul>
                </li>
                <li>
                    <strong>Nội dung không phù hợp với chuẩn mực đạo đức:</strong>
                    <ul>
                        <li>Không gửi hoặc truyền phát thông tin bất hợp pháp, lừa gạt, bôi nhọ, sỉ nhục, tục tĩu, khiêu dâm, xúc phạm, đe dọa, lăng mạ, thù hận, kích động hoặc trái với chuẩn mực đạo đức chung của xã hội.</li>
                        <li>Cấm miêu tả chi tiết các hành động dâm ô, bạo lực, giết người; đăng tải hình ảnh phản cảm, thiếu nhân văn hoặc cung cấp nội dung kích dục, không phù hợp với thuần phong mỹ tục Việt Nam.</li>
                    </ul>
                </li>
                <li>
                    <strong>Tuyên truyền quảng cáo:</strong>
                    <ul>
                        <li>Nghiêm cấm tuyên truyền bất kỳ thông điệp nào mang tính quảng cáo, mời gọi, thư dây chuyền hoặc cơ hội đầu tư không được người dùng mong muốn.</li>
                    </ul>
                </li>
                <li>
                    <strong>Quản lý nội dung:</strong>
                    <ul>
                        <li>Chúng tôi có quyền lược bớt hoặc xoá bỏ bất kỳ bài viết nào vi phạm quy định trên, bất kỳ vi phạm nào sẽ bị xử lý.</li>
                    </ul>
                </li>
                <li>
                    <strong>Bảo vệ quyền sở hữu và quyền riêng tư:</strong>
                    <ul>
                        <li>Không gửi hoặc truyền bất kỳ thông tin hoặc phần mềm nào không thuộc quyền sở hữu của bạn.</li>
                        <li>Cấm sử dụng thông tin vi phạm các quy định về sở hữu trí tuệ hoặc quy định khác của pháp luật hiện hành.</li>
                    </ul>
                </li>
                <li>
                    <strong>Không xâm phạm hệ thống:</strong>
                    <ul>
                        <li>Nghiêm cấm xâm nhập, tiếp cận, sử dụng hoặc cố gắng xâm nhập hệ thống của chúng tôi mà không được phép.</li>
                        <li>Cấm lợi dụng lỗi hệ thống để trục lợi cá nhân và tạo ra thông tin giả mạo.</li>
                    </ul>
                </li>
            </ol>
        </div>

        <div class="terms-section">
            <h2>Điều 4: Cơ chế xử lý thành viên vi phạm thỏa thuận</h2>
            <h3>1. Nguyên tắc xử lý</h3>
            <ul>
                <li>Nếu Người sử dụng vi phạm các điều khoản quy định tại Thỏa thuận cung cấp và sử dụng dịch vụ mạng xã hội hoặc theo cách khác tạo rủi ro hoặc trách nhiệm pháp lý có thể cho Hankyo, Người sử dụng sẽ được nhắc nhở, cảnh cáo, áp dụng biện pháp xử lý bài viết, khóa tài khoản có thời hạn hoặc vĩnh viễn.</li>
                <li>Hình thức xử phạt khóa tài khoản Hankyo có thời hạn hoặc vĩnh viễn, điều đó đồng nghĩa với việc Người sử dụng không thể sử dụng các sản phẩm khác khi truy cập từ tài khoản đã bị khóa. Một số trường hợp vi phạm nghiêm trọng Hankyo có thể chặn địa chỉ ip và không cần thông báo trước.</li>
                <li>Nếu tài khoản Hankyo bị khóa vĩnh viễn, thì toàn bộ những quyền lợi của Chủ tài khoản cũng sẽ khóa vĩnh viễn.</li>
                <li>Các hành vi vi phạm của Người sử dụng không được liệt kê ở dưới, tùy từng trường hợp cụ thể, Ban quản trị Hankyo có toàn quyền đưa ra mức phạt phù hợp theo các hình thức xử phạt theo quy định tại thỏa thuận này.</li>
                <li>Tất cả các bài viết vi phạm quy định có thể bị xoá, chuyển chuyên mục, lọc bỏ nội dung quảng cáo, thay từ ngữ không phù hợp, tạm ngừng hiển thị chờ kiểm duyệt tùy theo mức độ vi phạm. Hankyo có thể gửi thông báo cảnh báo nội dung vi phạm và yêu cầu Người sử dụng ngay lập tức gỡ bỏ, lọc bỏ nội dung vi phạm. Nếu Người sử dụng không gỡ bỏ, lọc bỏ nội dung vi phạm, Ban quản trị Hankyo xử lý bài viết và khóa tài khoản của Người sử dụng.</li>
                <li>Ban quản trị có toàn quyền trong việc xác định và xử lý bài viết/ thành viên vi phạm quy định. Nhật ký xử lý sẽ được lưu trữ tối thiểu hai năm, Ban quản trị có toàn quyền công khai các vi phạm của Thành viên nhưng không bao gồm nội dung vi phạm đã bị xóa hay lọc bỏ.</li>
            </ul>

            <h3>2. Các hình thức xử phạt:</h3>
            <div class="highlight">
                <h4>Hình thức xử phạt cấp đặc biệt, khóa vĩnh viễn được áp dụng bao gồm nhưng không giới hạn đối với các hành vi sau:</h4>
                <ul>
                    <li>Người sử dụng có hành vi lợi dụng Hankyo nhằm chống phá nước Cộng Hòa Xã Hội Chủ Nghĩa Việt Nam. Hành vi này bao gồm nhưng không giới hạn việc Người sử dụng đặt Tên tài khoản/ Tên thành viên trùng tên với các vĩ nhân, các vị anh hùng của dân tộc, các vị lãnh đạo của Đảng và Nhà nước, hoặc người dùng có sử dụng hình ảnh, video, phát ngôn … có chứa thông tin bàn luận về vấn đề chính trị hoặc tiết lộ bí mật nhà nước Cộng hòa Xã hội Chủ nghĩa Việt Nam.</li>
                    <li>Thông tin, hình ảnh, video khiêu dâm: Người sử dụng đăng tải hình ảnh, âm thanh, video khiêu dâm, chat sex hoặc tuyên truyền các các nội dung khiêu dâm.</li>
                    <li>Thông tin cá cược, cờ bạc: Lợi dụng Hankyo, người dùng đăng tải nội dung thông tin, hình ảnh, âm thanh, video chứa thông tin để tổ chức các hình thức cá cược, cờ bạc hoặc các thỏa thuận liên quan đến tiền, hiện kim, hiện vật.</li>
                    <li>Lan truyền thông tin lừa đảo: Sử dụng văn bản, hình ảnh, âm thanh hoặc video có chứa thông tin lừa đảo: giả làm chính thức hoặc các tổ chức, cá nhân; gian lận, lừa đảo tài sản của người khác hoặc tài khoản Hankyo.</li>
                </ul>
            </div>

            <div class="highlight">
                <h4>Hình thức xử phạt cấp 1: Khóa tài khoản 10 ngày</h4>
                <p>Được áp dụng bao gồm nhưng không giới hạn đối với các hành vi bình luận sai chủ để, spam trên diễn đàn.</p>
            </div>

            <div class="highlight">
                <h4>Hình thức xử phạt cấp 2: Khóa tài khoản 30 ngày</h4>
                <p>Được áp dụng bao gồm nhưng không giới hạn đối với các hành vi sau:</p>
                <ul>
                    <li>Hành vi giao tiếp: Khiêu dâm ở mức độ nhẹ, nhắn tin rác, kích động các thành viên khác đến các Nhóm thảo luận để gây rối hoặc tuyên truyền những thông tin vi phạm.</li>
                    <li>Quảng cáo: Trong tên thành viên, trạng thái, giải thích về bản thân có chứa thông tin quảng cáo hoặc sử dụng các văn bản, hình ảnh, âm thanh, hoặc video và thông tin quảng cáo mà không có sự đồng ý bằng văn bản của Hankyo. Ngoài ra thành viên sử dụng sản phẩm với mục đích quảng cáo, tuyên truyền, mua bán và truyền bá hàng hoá, dịch vụ bị cấm, trong trường hợp này tùy thuộc vào mức độ nghiêm trọng sẽ áp dụng hình phạt 2 hoặc hình phạt 3.</li>
                    <li>Xâm phạm riêng tư: Sử dụng hình ảnh cá nhân của người khác, công khai những tư liệu cá nhân và những thông tin của khác như danh tính, địa chỉ, số điện thoại mà chưa được sự đồng ý và tiến hành gọi điện quấy nhiễu hoặc khích động người khác quấy nhiễu.</li>
                    <li>Công kích người khác: Sử dụng hình ảnh, thông tin, âm thanh hoặc video, xúc phạm, đưa thông tin xuyên tạc, vu khống, nhạo báng, xúc phạm uy tín tới tổ chức, cá nhân.</li>
                    <li>Vi phạm bản quyền: Ăn cắp các nội dung, sao chép hoặc trích dẫn mà không được phép sử dụng bản quyền của người khác.</li>
                </ul>
            </div>

            <div class="highlight">
                <h4>Hình thức xử phạt cấp 3: Khóa tài khoản 120 ngày</h4>
                <p>Được áp dụng bao gồm nhưng không giới hạn đối với các hành vi sau:</p>
                <ul>
                    <li>Phá hoại hệ thống mạng xã hội Hankyo: Người sử dụng lợi dụng việc sử dụng sản phẩm để xâm nhập vào hệ thống máy chủ nhằm phá hoại sản phẩm hoặc cản trở việc truy cập thông tin. Người sử dụng sử dụng công cụ kỹ thuật nhằm tăng điểm hoạt động, vật phẩm hoặc nhằm treo máy, spam chat.</li>
                    <li>Sử dụng Diễn đàn thảo luận hay Hoạt động nhóm để lôi kéo tổ chức hội họp thực tế ở bên ngoài thực hiện các hành vi vi phạm pháp luật.</li>
                </ul>
            </div>
        </div>

        <div class="terms-section">
            <h2>CHÍNH SÁCH BẢO VỆ QUYỂN RIÊNG TƯ</h2>
            
            <h3>Điều 5: Quyền của người sử dụng dịch vụ mạng xã hội Hankyo</h3>
            <p>Được sử dụng dịch vụ của mạng xã hội Hankyo trừ các dịch vụ bị cấm theo quy định của pháp luật.</p>
            <ol>
                <li>Quyền thay đổi, bổ sung thông tin tài khoản; Mật khẩu; Giấy tờ tùy thân; Email đã đăng ký.</li>
                <li>Được hướng dẫn cách đặt mật khẩu an toàn; nhập các thông tin quan trọng để bảo vệ tài khoản; sử dụng tài khoản liên kết để đăng nhập tài khoản.</li>
                <li>Được quyền tặng tài khoản cho người khác. Quyền được tặng cho tài khoản chỉ được áp dụng đối với tài khoản đã đăng ký đầy đủ và chính xác các thông tin tài khoản theo quy định Thỏa thuận sử dụng Hankyo</li>
                <li>Được bảo vệ bí mật thông tin riêng và thông tin cá nhân theo quy định của pháp luật.</li>
            </ol>

            <h3>Điều 6: Trách nhiệm của người sử dụng dịch vụ mạng xã hội</h3>
            <ol>
                <li>Để nhận được sự hỗ trợ từ Hankyo, tài khoản của bạn phải đăng ký đầy đủ các thông tin trung thực, chính xác. Nếu có sự thay đổi, bổ sung về thông tin, bạn cập nhật ngay cho chúng tôi. Bạn đảm bảo rằng, thông tin hiện trạng của bạn là mới nhất, đầy đủ, trung thực và chính xác và bạn phải chịu trách nhiệm toàn bộ các thông tin bạn cung cấp.</li>
                <li>Bạn có trách nhiệm bảo mật thông tin tài khoản bao gồm: Mật khẩu, giấy tờ tùy thân, Email bảo vệ tài khoản và tài khoản liên kết. Nếu những thông tin trên bị tiết lộ dưới bất kỳ hình thức nào thì bạn phải chấp nhận những rủi ro phát sinh Hankyo sẽ căn cứ vào những thông tin hiện có trong tài khoản để làm căn cứ quyết định chủ sở hữu tài khoản nếu có tranh chấp và sẽ không chịu trách nhiệm về mọi tổn thất phát sinh. Thông tin Giấy tờ tùy thân đăng ký trong tài khoản là thông tin quan trọng nhất để chứng minh chủ sở hữu tài khoản.</li>
                <li>Bạn đồng ý sẽ thông báo ngay cho Hankyo về bất kỳ trường hợp nào sử dụng trái phép tài khoản và mật khẩu của bạn hoặc bất kỳ các hành động phá vỡ hệ thống bảo mật nào. Bạn cũng bảo đảm rằng, bạn luôn thoát tài khoản của mình sau mỗi lần sử dụng.</li>
                <li>Tuân thủ thỏa thuận quản lý, cung cấp và sử dụng dịch vụ mạng xã hội.</li>
                <li>Chịu trách nhiệm về nội dung thông tin do mình lưu trữ, cung cấp, truyền đưa trên mạng xã hội, phát tán thông tin qua đường liên kết trực tiếp do mình thiết lập.</li>
                <li>Khi phát hiện ra lỗi, các vấn đề gây ảnh hưởng tới hoạt động bình thường của Hankyo, bạn hãy thông báo cho chúng tôi qua số điện thoại 093 1600767</li>
                <li>Bạn cam kết thực hiện trách nhiệm bảo đảm sử dụng hợp pháp nội dung thông tin số đưa lên đăng tải trên hệ thống mạng Internet và mạng viễn thông.</li>
                <li>Bạn phải tuân thủ tuyệt đối quy định tại Điều 2 thỏa thuận này về các hành vi cấm. Nếu vi phạm một hoặc nhiều hành vi, tùy thuộc vào mức độ vi phạm Hankyo sẽ khóa tài khoản vĩnh viễn, tước bỏ mọi quyền lợi của bạn.</li>
                <li>Tuân thủ các quy định về bảo đảm an toàn thông tin, an ninh thông tin và các quy định khác có liên quan tại Nghị định 72/2013/NĐ-CP quy định về Quản lý, cung cấp, sử dụng dịch vụ internet và thông tin trên mạng.</li>
                <li>Thực hiện trách nhiệm khác theo quy định pháp luật.</li>
            </ol>

            <h3>Điều 7: Quyền của Công ty Cổ phần Công nghệ</h3>
            <ol>
                <li>Nếu Người sử dụng cung cấp bất kỳ thông tin nào không trung thực, không chính xác, hoặc nếu Công ty Cổ phần Công nghệ có cơ sở để nghi ngờ rằng thông tin đó không chính xác hoặc nếu Người sử dụng vi phạm bất cứ điều khoản nào trong thỏa thuận sử dụng hoặc thỏa thuận sử dụng các sản phẩm và dịch vụ khác của Hankyo được quy định trên website, Công ty Cổ phần Công nghệ có toàn quyền chấm dứt, xóa bỏ tài khoản của Người sử dụng mà không cần sự đồng ý của Người sử dụng và không phải chịu bất cứ trách nhiệm nào đối với Người sử dụng.</li>
                <li>Mọi vi phạm của Chủ tài khoản trong quá trình sử dụng sản phẩm Hankyo, Công ty Cổ phần Công nghệ có quyền áp dụng các biện pháp chế tài theo như điều khoản và thỏa thuận sử dụng này như gỡ bỏ, xóa nội dung vi phạm, có quyền tước bỏ mọi quyền lợi của Chủ tài khoản đối với việc sử dụng sản phẩm cũng như sẽ yêu cầu cơ quan chức năng truy tố Chủ tài khoản trước pháp luật nếu cần thiết.</li>
                <li>Khi phát hiện những vi phạm như gian lận, phá hoại, truyền bá nội dung cấm hoặc bất kỳ vi phạm quy định của pháp luật thông qua việc sử dụng Hankyo, Công ty Cổ phần Công nghệ có quyền sử dụng những thông tin cá nhân mà người sử dụng cung cấp khi đăng ký tài khoản để chuyển cho Cơ quan chức năng giải quyết theo quy định của pháp luật.</li>
                <li>Công ty Cổ phần Công nghệ có quyền từ chối hỗ trợ, giải quyết đối với những Tài khoản đăng ký thông tin không chính xác đầy đủ theo quy định và đối với những Tài khoản vi phạm trách nhiệm bảo mật tài khoản được quy định tại Thỏa thuận này.</li>
                <li>Có quyền áp dụng các quy định về điều khoản sử dụng dịch vụ bổ sung mà không cần phải được Người sử dụng đồng ý.</li>
                <li>Công ty Cổ phần Công nghệ có quyền thương mại hóa dịch vụ Hankyo tuy nhiên Công ty Cổ phần Công nghệ tuyệt đối tuân thủ thỏa thuận này. Công ty Cổ phần Công nghệ tuyệt đối không bán lại nội dung, thông tin của Người sử dụng. Trong trường hợp Công ty Cổ phần Công nghệ có thu phí sử dụng dịch vụ thì Công ty Cổ phần Công nghệ phải thông báo cho Người sử dụng và Người sử dụng phải tự nguyện đồng ý.</li>
                <li>Công ty Cổ phần Công nghệ có quyền bảo trì và nâng cấp hệ thống để phục vụ tốt hơn. Quá trình bảo trì hay nâng cấp có thể làm gián đoạn việc sử dụng dịch vụ hoặc chất lượng dịch vụ không đảm bảo trong thời điểm bảo trì hay nâng cấp.</li>
            </ol>
        </div>

        <div class="terms-section">
            <h3>Điều 8: Trách nhiệm của Công ty Cổ phần Công nghệ</h3>
            <ol>
                <li>Có trách nhiệm hỗ trợ chủ tài khoản trong quá trình sử dụng Hankyo.</li>
                <li>Nhận và giải quyết khiếu nại của người dùng các trường hợp phát sinh trong quá trình sử dụng mạng xã hội Hankyo tuy nhiên chúng tôi chỉ hỗ trợ, nhận và giải quyết đối với tài khoản đăng ký thông tin đầy đủ, chính xác, trung thực.</li>
                <li>Có trách nhiệm bảo mật thông tin cá nhân của chủ tài khoản Hankyo, không bán hoặc trao đổi những thông tin này với bên thứ 3, trừ trường hợp theo quy định pháp luật hoặc được chủ tài khoản chấp nhận.</li>
                <li>Công khai thỏa thuận cung cấp và sử dụng dịch vụ mạng xã hội.</li>
                <li>Có biện pháp bảo vệ bí mật thông tin riêng, thông tin cá nhân của người sử dụng; thông báo cho người sử dụng về quyền, trách nhiệm và các rủi ro khi lưu trữ, trao đổi và chia sẻ thông tin trên mạng.</li>
                <li>Bảo đảm quyền quyết định của người sử dụng khi cho phép thông tin cá nhân của mình được cung cấp cho tổ chức, doanh nghiệp, cá nhân khác.</li>
                <li>Không được chủ động cung cấp thông tin công cộng có nội dung vi phạm quy định tại Điều 5 Nghị định 72/2013/NĐ-CP quy định về Quản lý, cung cấp, sử dụng dịch vụ internet và thông tin trên mạng.</li>
                <li>Phối hợp với cơ quan quản lý nhà nước có thẩm quyền để loại bỏ hoặc ngăn chặn thông tin có nội dung vi phạm quy định tại Điều 5 Nghị định 72/2013/NĐ-CP quy định về Quản lý, cung cấp, sử dụng dịch vụ internet và thông tin trên mạng khi có yêu cầu.</li>
                <li>Cung cấp thông tin cá nhân và thông tin riêng của người sử dụng có liên quan đến hoạt động khủng bố, tội phạm, vi phạm pháp luật khi có yêu cầu của cơ quan quản lý nhà nước có thẩm quyền.</li>
                <li>Có ít nhất 01 hệ thống máy chủ đặt tại Việt Nam đáp ứng việc thanh tra, kiểm tra, lưu trữ, cung cấp thông tin theo yêu cầu của cơ quan quản lý nhà nước có thẩm quyền và giải quyết khiếu nại của người dùng đối với việc cung cấp dịch vụ theo quy định của Bộ Thông tin và Truyền thông.</li>
                <li>Thực hiện việc đăng ký, lưu trữ và quản lý thông tin cá nhân của người thiết lập trang thông tin điện tử cá nhân và người cung cấp thông tin khác trên mạng xã hội theo quy định của Bộ Thông tin và Truyền thông. Bảo đảm chỉ những người đã cung cấp đầy đủ, chính xác thông tin cá nhân theo quy định mới được thiết lập trang thông tin điện tử cá nhân hoặc cung cấp thông tin trên mạng xã hội.</li>
                <li>Báo cáo theo quy định và chịu sự thanh tra, kiểm tra của các cơ quan quản lý nhà nước có thẩm quyền.</li>
                <li>Trong trường hợp bất khả kháng Migii phải dừng cung cấp dịch vụ thì sẽ miễn trừ trách nhiệm với dữ liệu và gói mua premium.</li>
            </ol>

            <h2>QUYỀN VÀ TRÁCH NHIỆM CÁC BÊN</h2>
            
            <h3>Điều 9. Mục đích thu thập Thông tin cá nhân</h3>
            <ul>
                <li>Thực hiện quản lý việc đăng thông tin và kiểm duyệt thông tin công khai.</li>
                <li>Thực hiện quản lý lưu lượng người dùng sử dụng dịch vụ, số lượng người dùng.</li>
                <li>Thực hiện quản lý hoạt động tiếp thị, cung cấp thông tin tới người dùng như gửi các cập nhật mới nhất về dịch vụ diễn đàn và các tính năng mới liên quan đến sản phẩm và dịch vụ của chúng tôi.</li>
                <li>Cung cấp giải pháp và thay đổi dịch vụ nhằm phục vụ nhu cầu người dùng.</li>
                <li>Quản lý, phân tích, đánh giá số liệu liên quan đến dữ liệu hệ thống.</li>
                <li>Tiếp nhận thông tin, góp ý, đề xuất, khiếu nại của thành viên nhằm cải thiện chất lượng dịch vụ.</li>
                <li>Liên hệ với thành viên để giải quyết các yêu cầu của thành viên.</li>
            </ul>

            <h3>Điều 10. Phạm vi thu thập thông tin cá nhân</h3>
            <h4>Loại thông tin thu thập:</h4>
            <ul>
                <li>Thông tin cá nhân</li>
                <li>Thông tin liên lạc như số điện thoại, địa chỉ gửi thư, địa chỉ email, số fax</li>
            </ul>

            <h4>Nội dung thông tin thu thập:</h4>
            <ul>
                <li>Họ và tên</li>
                <li>Ngày, tháng, năm sinh</li>
                <li>Số chứng minh nhân dân hoặc số hộ chiếu, ngày cấp, nơi cấp</li>
                <li>Số điện thoại, địa chỉ email</li>
            </ul>

            <div class="highlight">
                <p>Người dùng có quyền từ chối hoặc không cung cấp đầy đủ các thông tin được yêu cầu. Trong trường hợp đó, Chúng tôi không thể đảm bảo cho người dùng những dịch vụ đầy đủ và chất lượng.</p>
                <p>Chúng tôi sẽ yêu cầu bạn cung cấp thông tin cá nhân khi bạn đăng ký làm thành viên của website chúng tôi (Account). Nếu sử dụng thông tin này cho những mục đích khác ngoài mục đích thu thập, khi đó chúng tôi sẽ xin phép bạn trước khi sử dụng những thông tin này.</p>
            </div>
        </div>

        <div class="terms-section">
            <h3>Điều 11: Phương pháp thu thập thông tin</h3>
            <p>Mạng xã hội Hankyo, thu thập các Thông tin cá nhân của thành viên nêu tại Điều 10 nêu trên qua website thông qua Form Đăng ký thành viên được cung cấp công khai tại địa chỉ Hankyo</p>

            <h3>Điều 12: Thời gian lưu trữ thông tin thu thập</h3>
            <p>Mạng xã hội Hankyo sẽ Lưu trữ tối thiểu 02 (hai) năm đối với các thông tin về tài khoản, thời gian đăng nhập, đăng xuất, địa chỉ IP của người sử dụng và nhật ký xử lý thông tin được đăng tải. Trong mọi trường hợp thông tin cá nhân thành viên sẽ được bảo mật trên máy chủ của Hankyo</p>

            <h3>Điều 13: Việc công khai, chỉnh sửa thông tin thu thập</h3>
            <p>Chúng tôi cấp quyền cho thành viên tự chỉnh sửa thông tin công khai trên website. Chúng tôi chỉ hủy bỏ hoặc ẩn thông tin khi thành viên có yêu cầu hoặc thông tin vi phạm Quy định của mạng xã hội hội Hankyo</p>
            <p>Chúng tôi công bố các thông tin cá nhân thu thập từ người dùng lên website Hankyo và website liên quan nhằm mục đích cung cấp dịch vụ tốt nhất cho người dùng và đảm bảo tính hiệu quả của dịch vụ.</p>
            <p>Chúng tôi có thể phải cung cấp các thông tin cá nhân của người dùng cho các cơ quan chức năng, cơ quan của Chính phủ vì các mục đích an toàn an ninh, hải quan, nhập cư và các mục đích theo luật định khác trong phạm vi được yêu cầu hoặc theo luật định.</p>

            <h3>Điều 14. Địa chỉ của đơn vị thu thập và quản lý thông tin cá nhân</h3>
            <p>Công ty Cổ phần Công nghệ.</p>
            <ul>
                <li>Trụ sở chính: Đ. Nam Kỳ Khời Nghĩa, Khu đô thị FPT City, Ngũ Hành Sơn, Đà Nẵng.</li>
                <li>Tel: 093 1600767</li>
                <li>Email: hankyoo@eupgroup.net</li>
            </ul>
            <p>Hankyo chỉ đứng dưới góc độ là 1 sàn thương mại điện tử cung cấp thông tin từ các đơn vị đã được Hankyo kiểm định mức độ uy tín trong ngành.</p>

            <h3>Điều 15. Cam kết bảo mật thông tin</h3>
            <ul>
                <li>Chúng tôi sử dụng các biện pháp bảo mật thích hợp để bảo vệ việc truy cập hoặc sửa đổi trái phép, tiết lộ hoặc huỷ dữ liệu. Các biện pháp này bao gồm xem xét thực tiễn thu thập, lưu trữ, xử lý và biện pháp bảo mật dữ liệu nội bộ, cũng như các biện pháp bảo mật mức vật lý để bảo vệ việc truy cập trái phép vào hệ thống lưu trữ dữ liệu cá nhân.</li>
                <li>Chúng tôi chỉ cho phép nhân viên có thẩm quyền của mạng xã hội Hankyo truy cập thông tin cá nhân; những người cần biết thông tin này để vận hành, phát triển và cải tiến dịch vụ của chúng tôi. Những cá nhân này bị ràng buộc bởi nghĩa vụ bảo mật và có thể chịu kỷ luật, bao gồm chấm dứt việc làm và truy tố hình sự, khi họ vi phạm các nghĩa vụ này.</li>
                <li>Mỗi thành viên tự chịu trách nhiệm bảo mật mật khẩu tài khoản mạng xã hội Hankyo, cam kết không công khai hay cung cấp thông tin bảo mật tài khoản của thành viên đến bất kỳ ai hay đơn vị thứ ba nào khác. Thông tin về tài khoản của bạn được đặt trên một máy chủ rất an toàn và được tường lửa bảo vệ.</li>
                <li>Quy định về quyền riêng tư có thể có những thiếu sót nhất định, do trục trặc kỹ thuật, hoặc các vấn đề bất khả kháng và mạng xã hội Hankyo hoàn toàn không chịu trách nhiệm về những thiệt hại do các lỗi đó gây ra. Hãy luôn luôn cẩn trọng và tự bảo vệ thông tin cá nhân của bạn.</li>
                <li>Chúng tôi sẽ bắt buộc phải công khai thông tin nếu đó là việc cần thiết để bảo vệ tài sản cũng như sự an toàn của mạng xã hội Hankyo hoặc những người khác cũng như ngăn ngừa các hoạt động phạm pháp, vi phạm quy định làm ảnh hưởng lớn tới các thành viên khác. Hoặc khi cơ quan thực thi pháp luật yêu cầu cung cấp thông tin.</li>
                <li>Người đăng tin sẽ chịu trách nhiệm về tính xác thực, và chịu trách nhiệm về mọi tranh chấp pháp lý xảy ra liên quan đến thông tin của mình.</li>
            </ul>

            <h3>Điều 16. Chính sách bảo vệ thông tin cá nhân, thông tin riêng của người sử dụng dịch vụ mạng xã hội</h3>
            <ul>
                <li>Chúng tôi công khai các thông tin quy định tại Điều 9, 10, 11, 12, 13, 14, 15 tại bản thỏa thuận này để người dùng nắm được chính sách bảo vệ quyền riêng tư của mạng xã hội Hankyo</li>
                <li>Để bảo vệ thông tin cá nhân, thông tin riêng của người sử dụng dịch vụ mạng xã hội chúng tôi sử dụng các phần mềm lưu trữ, phần mềm bảo mật thông tin và hệ thống tường lửa rất chắc chắn. Đảm bảo thông tin của quý khách hàng không bị đánh cắp.</li>
            </ul>
        </div>

        <div class="terms-section">
            <h2>GIẢI QUYẾT, TRANH CHẤP KHIẾU NẠI</h2>
            
            <h3>Điều 17. Cơ chế giải quyết tranh chấp</h3>
            <ul>
                <li>Mọi tranh chấp phát sinh giữa mạng xã hội Hankyo và thành viên hoặc nhà cung cấp sẽ được giải quyết trên cơ sở thương lượng. Trường hợp không đạt được thỏa thuận như mong muốn, một trong hai bên có quyền đưa vụ việc ra Tòa án để giải quyết.</li>
                <li>Khi tranh chấp phát sinh giữa các thành viên hoặc giữa thành viên với nhà cung cấp dịch vụ trực tiếp, ban quản lý website sẽ có trách nhiệm cung cấp cho thành viên thông tin về nhà cung cấp dịch vụ, tích cực hỗ trợ thành viên hoặc đại diện thành viên bảo vệ quyền lợi và lợi ích hợp pháp của mình.</li>
                <li>Trong trường hợp xảy ra sự cố do lỗi của Hankyo chúng tôi sẽ ngay lập tức áp dụng các biện pháp cần thiết để đảm bảo quyền lợi cho người dùng.</li>
            </ul>

            <h3>Điều 18. Quy trình giải quyết tranh chấp</h3>
            <p><strong>Bước 1:</strong> Thành viên mạng xã hội khiếu nại về dịch vụ hoặc thông báo các tranh chấp giữa các bên qua email: quoctan.se@gmail.com</p>
            <p><strong>Bước 2:</strong> Bộ phận bảo vệ quyền lợi thành viên của mạng xã hội Hankyo sẽ tiếp nhận các khiếu nại của thành viên, tùy theo tính chất và mức độ của khiếu nại thì Hankyo sẽ có những biện pháp cụ thể hộ trợ thành viên để giải quyết tranh chấp đó.</p>
            <p><strong>Bước 3:</strong> Trong trường nằm ngoài khả năng và thẩm quyền của Hankyo thì ban quản trị sẽ yêu cầu thành viên đưa vụ việc này ra cơ quan nhà nước có thẩm quyền giải quyết theo pháp luật.</p>

            <h4>Thời gian giải quyết tranh chấp:</h4>
            <ul>
                <li>Trường hợp nằm trong khả năng và thẩm quyền của Hankyo chúng tôi cam kết sẽ giải quyết tranh chấp của thành viên trong vòng 5 ngày làm việc.</li>
                <li>Trường hợp nằm ngoài khả năng và thẩm quyền của Hankyo thì thời gian giải quyết tranh chấp tùy thuộc vào tính chất vụ việc và cơ quan nhà nước có thẩm quyền.</li>
            </ul>

            <h3>Điều 19. Vai trò và trách nhiệm của các bên trong việc giải quyết tranh chấp</h3>
            <ul>
                <li>Mạng xã hội Hankyo sẽ hỗ trợ thành viên bằng việc cung cấp đầy đủ thông tin và các nội dung liên quan đến nhà cung cấp dịch vụ trên mạng xã hội.</li>
                <li>Mạng xã hội Hankyo có trách nhiệm tiếp nhận khiếu nại và có biện pháp xử lý kịp thời để giải quyết những mâu thuẫn xảy ra giữa thành viên và bên thứ ba.</li>
                <li>Nhà cung cấp dịch vụ trên mạng xã hội Hankyo có trách nhiệm cung cấp đầy đủ thông tin liên quan đến quá trình giao dịch và có nghĩa vụ bồi thường theo thỏa thuận với thành viên mạng xã hội Hankyo nếu lỗi thuộc về nhà cung cấp dịch vụ.</li>
                <li>Mọi hành động lừa đảo, gây mâu thuẫn trên mạng xã hội Hankyo đều bị lên án và phải chịu trách nhiệm trước pháp luật.</li>
                <li>Nếu thông qua hình thức thỏa thuận mà vẫn không thể giải quyết được mâu thuẫn phát sinh thì sẽ chuyển cho cơ quan nhà nước có thẩm quyền can thiệp nhằm đảm bảo lợi ích hợp pháp của các bên. Đồng thời mạng xã hội Hankyo có trách nhiệm hỗ trợ Cơ quan pháp luật bằng việc cung cấp thông tin về các bên quá trình điều tra.</li>
            </ul>

            <h3>Điều 20. Chỉnh sửa bổ sung thỏa thuận</h3>
            <p>Các điều khoản quy định tại Thỏa thuận sử dụng mạng xã hội Hankyo được quy định trên website, có thể được cập nhật, chỉnh sửa bất cứ lúc nào mà không cần phải thông báo trước tới người sử dụng. Hankyo sẽ công bố rõ trên Website về những thay đổi, bổ sung đó, người dùng có nghĩa vụ cập nhật thường xuyên.</p>

            <h3>Điều 21. Xung đột pháp luật</h3>
            <p>Trong trường hợp một hoặc một số điều khoản Thỏa thuận sử dụng này xung đột với các quy định của luật pháp và bị Tòa án tuyên là vô hiệu, điều khoản đó sẽ được chỉnh sửa cho phù hợp với quy định pháp luật hiện hành, và phần còn lại của Thỏa thuận sử dụng vẫn giữ nguyên giá trị.</p>

            <h3>Điều 22. Cảnh báo</h3>
            <ol>
                <li>
                    <p>Mạng xã hội hội Hankyo cảnh báo cho tất cả người sử dụng các rủi ro có thể gặp phải khi lưu trữ, trao đổi và chia sẻ thông tin trên mạng. Thông tin của khách hàng có thể bị tin tặc tấn công và mạo danh, đề đảm bảo an toàn thành viên không nên đăng tải những hình ảnh cá nhân nhạy cảm, những thông tin về bí mật đời tư, bí mật kinh doanh, thông tin về tài khoản ngân hàng...Bạn sở hữu tất cả các nội dung và thông tin bạn đăng trên Hankyo và bạn có thể kiểm soát việc chia sẻ thông tin đó thông qua thông tin cá nhân và cập nhật thông tin cá nhân. Ngoài ra:</p>
                    <ul>
                        <li>Đối với những nội dung có quyền sở hữu trí tuệ như ảnh, Audio và video (nội dung IP), bạn mặc định cho chúng tôi bản quyền của bạn: bạn cấp cho chúng tôi một giấy phép không độc quyền, có thể chuyển nhượng, có thể cấp phép lại, miễn phí, toàn cầu để sử dụng mọi nội dung IP mà bạn đăng trên hoặc liên quan tới Hankyo. Và kết thúc khi bạn xóa nội dung IP hoặc tài khoản của bạn trừ khi nội dung của bạn được chia sẻ với người khác và những người đó chưa xóa nội dung đó.</li>
                        <li>Khi bạn công bố nội dung hoặc thông tin bằng cách điền thông tin cá nhân hoặc cập nhật thông tin cá nhân, có nghĩa là bạn đang cho phép mọi người bao gồm những người là thành viên của Hankyo, truy cập, sử dụng thông tin đó.</li>
                        <li>Chúng tôi luôn tiếp nhận phản hồi hoặc các đề xuất khác của bạn về Hankyo nhưng bạn cần hiểu rằng chúng tôi có thể sử dụng các phản hồi và đề xuất đó mà không có nghĩa vụ phải đền bù cho bạn (cũng như bạn không có nghĩa vụ phải cung cấp các phản hồi và đề xuất đó).</li>
                    </ul>
                </li>
                <li>
                    <p><strong>An toàn</strong></p>
                    <p>Chúng tôi nỗ lực hết sức mình để tạo an toàn cho Hankyo nhưng chúng tôi không thể đảm bảo điều đó. Chúng tôi cần sự giúp đỡ của bạn để tạo an toàn cho Hankyo, với các cam kết sau từ phía bạn:</p>
                    <ul>
                        <li>Bạn sẽ không vi phạm quy định như spam trên Hankyo</li>
                        <li>Bạn sẽ không tham gia vào hoạt động tiếp thị bất hợp pháp như quảng cáo đồi trụy… trên Hankyo</li>
                        <li>Bạn sẽ không tải lên virus hoặc mã độc hại khác.</li>
                        <li>Bạn sẽ không hack thông tin đăng nhập hoặc tài khoản của người khác.</li>
                        <li>Bạn sẽ không hăm dọa hoặc quấy rối bất kỳ người dùng nào.</li>
                        <li>Bạn sẽ không đăng nội dung: có ngôn từ kích động thù địch, mang tính đe dọa hay khiêu dâm; kích động bạo lực hoặc chứa ảnh khỏa thân hoặc hình ảnh bạo lực hay nội dung bạo lực vô cớ.</li>
                        <li>Bạn sẽ không dùng Hankyo để làm bất kỳ điều gì bất hợp pháp, sai trái, độc hại hoặc phân biệt đối xử.</li>
                        <li>Bạn sẽ không làm bất kỳ điều gì có thể làm vô hiệu, quá tải hoặc ảnh hưởng đến hệ thống của Hankyo</li>
                    </ul>
                </li>
                <li>
                    <p><strong>Đăng ký và bảo mật tài khoản</strong></p>
                    <p>Người dùng Hankyo phải cung cấp đầy đủ thông tin bắt buộc khi đăng ký tài khoản Sau đây là một số cam kết của bạn đối với chúng tôi liên quan đến việc đăng ký và duy trì bảo mật tài khoản của bạn:</p>
                    <ul>
                        <li>Bạn sẽ không cung cấp mọi thông tin cá nhân sai lệch trên Hankyo hoặc tạo tài khoản cho bất kỳ ai khác ngoài bạn mà không được phép.</li>
                        <li>Bạn sẽ không tạo nhiều hơn một tài khoản cá nhân.</li>
                        <li>Nếu chúng tôi vô hiệu tài khoản của bạn, bạn sẽ không tạo một tài khoản khác mà không có sự cho phép của chúng tôi.</li>
                        <li>Bạn sẽ không dùng Hankyo nếu bạn bị kết án phạm tội tình dục.</li>
                        <li>Bạn sẽ duy trì thông tin liên hệ của mình chính xác và cập nhật.</li>
                        <li>Bạn sẽ không chia sẻ mật khẩu của mình, không cho bất kỳ ai truy cập vào tài khoản của mình hoặc làm bất kỳ điều gì có thể gây nguy hiểm cho việc bảo mật tài khoản của mình.</li>
                    </ul>
                </li>
            </ol>

            <h3>Điều 23. Việc cập nhật của Chính sách bảo mật</h3>
            <p>Mạng xã hội Hankyo sẽ chỉnh sửa Chính sách bảo mật này vào bất kỳ thời điểm nào khi cần thiết, bản Chính sách bảo mật cập nhật sẽ được công bố trên website Hankyo và sẽ được ghi ngày để người dùng nhận biết được bản mới nhất.</p>

            <h3>Điều 24. Địa chỉ liên hệ</h3>
            <p>Nếu bạn có bất kì câu hỏi nào liên quan đến quá trình xử lí thông tin của bạn hay Chính sách bảo mật này, vui lòng liên hệ với chúng tôi qua số điện thoại hoặc tại địa chỉ: Đ. Nam Kỳ Khời Nghĩa, Khu đô thị FPT City, Ngũ Hành Sơn, Đà Nẵng, Việt Nam.</p>

            <h3>Điều 25: Thỏa thuận cung cấp sử dụng mạng xã hội Hankyo có giá trị từ ngày 06 tháng 06 năm 2025.</h3>
        </div>

        <div class="payment-section">
            <h2>Hướng dẫn thanh toán</h2>
            <p>Bạn vui lòng chuyển khoản cho chúng tôi theo một trong các hình thức sau đây.</p>
            <p>Sau khi xác nhận thông tin chuyển khoản, khóa học của bạn sẽ được kích hoạt tự động ngay lập tức. Bạn có thể bắt đầu học mà không cần chờ mã kích hoạt.</p>
            
            <div class="bank-details">
                <h3>Ngân hàng ACB - Ngân hàng thương mại cổ phần Á Châu ACB</h3>
                <p><strong>Chủ TK:</strong> Công Ty TNHH Công Nghệ A Plus</p>
                <p><strong>Số tài khoản:</strong> 3333 588 588</p>
                <p><strong>Nội dung chuyển khoản:</strong> Tên + SĐT đặt hàng</p>
            </div>

            <div class="contact-info">
                <h3>Hỗ trợ</h3>
                <p>Trong trường hợp hệ thống gặp sự cố hoặc bạn cần hỗ trợ, vui lòng liên hệ với chúng tôi qua:</p>
                <ul>
                    <li>Email: hankyoo@eupgroup.net</li>
                    <li>Hotline: 093 1600767</li>
                </ul>
                <p class="highlight">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7!</p>
            </div>
        </div>

        <div class="terms-section">
            <p class="highlight">Khi đăng ký học, bạn xác nhận đã đọc và đồng ý với Điều khoản và điều kiện giao dịch của HANKYO.</p>
        </div>

        <div class="footer-section">
            <p class="text-center mt-4">Bản quyền © 2024 Hankyo. Tất cả các quyền được bảo lưu.</p>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
