(ns ragtacts.legacy.embedder.open-ai
  (:require [ragtacts.legacy.embedder.base :refer [Embedder make-embedding]]
            [wkok.openai-clojure.api :as openai]))

(defrecord OpenAIEmbedder [model]
  Embedder
  (embed [_ chunks]
    (try
      (let [texts (map :text chunks)
            {:keys [data]} (openai/create-embedding {:model model
                                                     :input texts})]
        (mapv (fn [{:keys [embedding]} {:keys [doc-id metadata text]}]
                (make-embedding doc-id
                                text
                                (map float embedding)
                                metadata))
              data
              chunks))
      (catch Exception e
        (.printStackTrace e)))))

(defn make-open-ai-embedder [{:keys [model]}]
  (->OpenAIEmbedder model))
