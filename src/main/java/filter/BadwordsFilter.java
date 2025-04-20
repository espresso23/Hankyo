/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ADMIN
 */
public class BadwordsFilter {
    // Define the comprehensive bad words list here
    private static final List<String> badWords = Arrays.asList(
            "cc", "Cc", "CC", "cC",
            "vcl", "Vcl", "VCl", "vCl", "VCL",
            "cl", "CL",
            "dcm", "Dcm", "DcM", "DCM",
            "dcmm", "Dcmm", "DcMM", "DCMM", "dCmM",
            "dit", "Dit", "DIT", "diT", "dIt", "dIT", "DIt",
            "me", "Me", "ME", "mE",
            "lon", "Lon", "LON", "lOn", "lON", "loN",
            "cac", "Cac", "CAC", "CaC", "cAC", "caC",
            "địt", "Địt", "ĐỊT", "ĐỊt", "Địt",
            "mẹ", "Mẹ", "MẸ", "mẹ", "ME", "mE",
            "lồn", "Lồn", "LỒN", "Lồn", "LoN",
            "cặc", "Cặc", "CẶC", "CăC", "cẶC",
            "má", "Má", "MÁ",
            "đụ", "Đụ", "ĐỤ", "đỤ", "dỤ",
            "đéo", "Đéo", "ĐÉO", "Đéo", "ĐÉo",
            "chó", "Chó", "CHÓ", "C chó",
            "mả cha", "Mả cha", "Mả Cha", "MẢ CHA",
            "cmnr", "CMnr", "CMNR", "cmnR", "CmNr", "Cmnr",
            "chịch", "Chịch", "CHỊCH", "ChịCH", "cHịch",
            "CLGT", "Clgt", "clgt", "clGT", "CLgt",
            "CĐGT", "cđgt", "Cđgt", "Cdgt", "CDGT",
            "cdgt", "cDGT", "CdGt", "Cdgt", "CdgT",
            "fuck", "Fuck", "FUck", "FUCk", "FUCK", "fUCK", "fuCK",
            "cứt", "Cứt", "CỨT", "CứT",
            "cut", "Cut", "CUT", "cUt", "CuT",
            "cu", "CU", "Cu", "cU",
            "phò", "Phò", "PHò", "PHÒ", "pho", "Pho", "PHo", "pHo",
            "đệt", "Đệt", "ĐỆT", "ĐỆt",
            "dí", "Dí", "DÍ",
            "đĩ", "Đĩ", "ĐĨ",
            "chó đẻ", "Chó Đẻ", "CHÓ ĐẺ", "chó Đẻ",
            "khùng", "Khùng", "KHÙNG",
            "khung", "Khung", "KHUNG",
            "điên", "Điên", "ĐIÊN",
            "dien", "Dien", "DIEN",
            "diên", "Diên", "DIÊN",
            "mọi", "Mọi", "MỌI",
            "moi", "Moi", "MOI",
            "súc vật", "Súc Vật", "SÚC VẬT", "súc Vật",
            "suc vat", "Suc Vat", "SUC VAT", "suc VAT",
            "sv", "Sv", "SV",
            "svat", "Svat", "SVAT",
            "buoi", "Buoi", "BUOI",
            "buồi", "Buồi", "BUỒI",
            "xạo lồn", "Xạo Lồn", "XẠO LỒN",
            "xao lon", "Xao Lon", "XAO LON",
            "xạo cặc", "Xạo Cặc", "XẠO CẶC",
            "xao cac", "Xao Cac", "XAO CAC",
            "chết mẹ", "Chết Mẹ", "CHẾT MẸ",
            "chet me", "Chet Me", "CHET ME",
            "xạo lìn", "Xạo Lìn", "XẠO LÌN",
            "xao lin", "Xao Lin", "XAO LIN",
            "á đù", "Á Đù", "Á ĐÙ",
            "a du", "A Du", "A DU",
            "á du", "Á Du", "Á DU",
            "a đù", "A Đù", "A ĐÙ",
            "damn", "Damn", "DAMN",
            "ỉa", "Ỉa", "ỈA",
            "ia", "Ia", "IA",
            "đái", "Đái", "ĐÁI",
            "dái", "Dái", "DÁI",
            "bìu", "Bìu", "BÌU",
            "biu", "Biu", "BIU",
            "đis", "Đis", "ĐIS",
            "dis", "Dis", "DIS"
    );

    public static String censorBadWords(String message) {
        String censoredMessage = message;
        for (String badWord : badWords) {
            String regex = "\\b" + Pattern.quote(badWord) + "\\b";
            String replacement = "*".repeat(badWord.length());
            censoredMessage = censoredMessage.replaceAll(regex, replacement);
        }
        return censoredMessage;
    }
}
