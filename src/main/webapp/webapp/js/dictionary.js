function addFavoriteFlashCard(wordID){
    $.ajax({
        url: "dictionary",
        type: "POST",
        data: {wordID: wordID, action: "addFavoriteFlashCard"}
        }
    )
}