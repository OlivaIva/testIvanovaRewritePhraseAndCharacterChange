require: scripts/funcs.js
require: answers.yaml
    var = answers

theme: /

        
    state: Hello
        q!: * ([включи*/запуст*] шаблон ~персонаж/start) Афина:afina *
        q!: * ([включи*/запуст*] шаблон ~персонаж/start) Джой:joy *
        q!: * ([включи*/запуст*] шаблон ~персонаж/start) Сбер:sber *
        q!: * ([включи*/запуст*] шаблон ~персонаж/start/в начало) *
        q!: * (~помощь/~справка/помоги*/help/хелп/хэлп/~меню/може*/умее*):help *
        script:

            if ($parseTree && $parseTree.value != "help") {
                $jsapi.startSession();
                $session.character = $parseTree.value == "afina" ? "Афина" : $parseTree.value == "joy" ? "Джой" : "Сбер";
            }
            $reactions.answer(answers.Greetings[$session.character]);
            
        go!: /DoYouWantRewrite
        
    state: DoYouWantRewrite
        q!: * (переписать/переделать/преобразовать) (~предложение/фраз*) *
        q!: * предложи вариант* (предложения/фразы)*
        q!: * сгенерировать парафраз *
        script:
            $reactions.answer(answers.DoYouWantRewrite[$session.character]);
        
        state: Yes
            intent: /Agree
            go!: /AskForSentenceToRewrite
        
        state: No
            intent: /Disagree
            script:
                $reactions.answer(answers.Disagree[$session.character]);

    
    state: AskForSentenceToRewrite
        script:
            $reactions.answer(answers.WhatToRewrite[$session.character]);
            
        state: GetAskForSentence
            q: *
            script:
                var sentence = $request.query;
                $temp.response = RewriteSentence(sentence);
                if ($temp.response != undefined && $temp.response != false && $temp.response.prediction_best && $temp.response.prediction_best.bertscore && $temp.response.predictions_all && typeof $temp.response.predictions_all === 'object'){
                    $reactions.answer("Лучший результат: " + $temp.response.prediction_best.bertscore);
                    $reactions.answer("Все варианты: " + $temp.response.predictions_all.join('\n'));
                } else {
                    $reactions.answer("У меня ничего не вышло.");
                }
                $reactions.transition("/DoYouWantMore");
    
    state: DoYouWantMore
        script:
            $reactions.answer(answers.WantMore[$session.character]);
        
        
        state: Yes
            intent: /Agree
            go!: /AskForSentenceToRewrite
    
        state: No
            intent: /Disagree
            script:
                $reactions.answer(answers.Disagree[$session.character]);
                
                
    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Я не понимаю.
            a: Для меня это звучит непонятно.
            a: Ничего не пойму

                
                
                