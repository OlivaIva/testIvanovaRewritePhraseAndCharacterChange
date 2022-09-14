function RewriteSentence(sentence) {
    var funcOptions = {
        headers: {
            "Content-Type": "application/json"
    },
        body: {
            "instances": [
            {
              "text": sentence,
              "temperature": 0.9,
              "top_k": 50,
              "top_p": 0.7,
              "range_mode": "bertscore"
            }
            ]
        }
    }
    var response = $http.post("https://api.aicloud.sbercloud.ru/public/v2/rewriter/predict", funcOptions);
    return response.isOk ? response.data : false;
}    
