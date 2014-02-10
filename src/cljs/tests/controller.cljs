(ns jasmine-cljs.controller)

(def app (.module js/angular "myApp" (array)))

(defn controller [$scope]
  (aset $scope "spice" "habanero"))

(.controller app "MyController" (array "$scope" controller))
