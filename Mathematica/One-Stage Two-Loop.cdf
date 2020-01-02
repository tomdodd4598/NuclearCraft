(* Content-type: application/vnd.wolfram.cdf.text *)

(*** Wolfram CDF File ***)
(* http://www.wolfram.com/cdf *)

(* CreatedBy='Mathematica 10.2' *)

(*************************************************************************)
(*                                                                       *)
(*  The Mathematica License under which this file was created prohibits  *)
(*  restricting third parties in receipt of this file from republishing  *)
(*  or redistributing it by any means, including but not limited to      *)
(*  rights management or terms of use, without the express consent of    *)
(*  Wolfram Research, Inc. For additional information concerning CDF     *)
(*  licensing and redistribution see:                                    *)
(*                                                                       *)
(*        www.wolfram.com/cdf/adopting-cdf/licensing-options.html        *)
(*                                                                       *)
(*************************************************************************)

(*CacheID: 234*)
(* Internal cache information:
NotebookFileLineBreakTest
NotebookFileLineBreakTest
NotebookDataPosition[      1064,         20]
NotebookDataLength[     23632,        477]
NotebookOptionsPosition[     24202,        473]
NotebookOutlinePosition[     24636,        492]
CellTagsIndexPosition[     24593,        489]
WindowFrame->Normal*)

(* Beginning of Notebook Content *)
Notebook[{
Cell[BoxData[
 TagBox[
  StyleBox[
   DynamicModuleBox[{$CellContext`closedLoop$$ = 
    True, $CellContext`condensateWaterTubeConductivity$$ = 
    1, $CellContext`coolantHeatMult$$ = 125, $CellContext`coolantPerRecipe$$ =
     20, $CellContext`coolantTubeConductivity$$ = 
    1, $CellContext`hpSteamTubeConductivity$$ = 
    1, $CellContext`lqSteamCondenserConductivity$$ = 
    1, $CellContext`lqSteamCondenserSurroundingTemp$$ = 
    300, $CellContext`msrBaseCoolingRate$$ = 
    480, $CellContext`msrCoolingEfficiency$$ = 
    1.87333, $CellContext`noHeaters$$ = 8, $CellContext`preheated$$ = 
    True, $CellContext`waterTubeConductivity$$ = 1, Typeset`show$$ = True, 
    Typeset`bookmarkList$$ = {}, Typeset`bookmarkMode$$ = "Menu", 
    Typeset`animator$$, Typeset`animvar$$ = 1, Typeset`name$$ = 
    "\"untitled\"", Typeset`specs$$ = {{
      Hold[
       Style["Configurable Constants", Bold, Medium]], 
      Manipulate`Dump`ThisIsNotAControl}, {{
       Hold[$CellContext`coolantPerRecipe$$], 20, "Coolant Per Recipe"}}, {{
       Hold[$CellContext`coolantHeatMult$$], 125, 
       "Coolant Heat Multiplier"}}, {
      Hold[
       Button[
       "reset configs", {$CellContext`coolantPerRecipe$$ = 
         20, $CellContext`coolantHeatMult$$ = 125}]], 
      Manipulate`Dump`ThisIsNotAControl}, {
      Hold[
       Style["Design Parameters", Bold, Medium]], 
      Manipulate`Dump`ThisIsNotAControl}, {{
       Hold[$CellContext`preheated$$], True, "Preheated Water"}, {
      True, False}}, {{
       Hold[$CellContext`closedLoop$$], True, "Closed Water/Steam Loop"}, {
      True, False}}, {{
       Hold[$CellContext`msrBaseCoolingRate$$], 480, 
       "MSR Coolant: Base Cooling Rate"}}, {{
       Hold[$CellContext`msrCoolingEfficiency$$], 1.87333, 
       "MSR Coolant: Cooling Efficiency"}}, {{
       Hold[$CellContext`noHeaters$$], 8, "MSR Number of Coolant Heaters"}}, {{
       Hold[$CellContext`coolantTubeConductivity$$], 1, 
       "Coolant Exchanger Tube Conductivity"}}, {{
       Hold[$CellContext`waterTubeConductivity$$], 1, 
       "Water Exchanger Tube Conductivity"}}, {{
       Hold[$CellContext`hpSteamTubeConductivity$$], 1, 
       "HP Steam Exchanger Tube Conductivity"}}, {{
       Hold[$CellContext`lqSteamCondenserConductivity$$], 1, 
       "Exhaust Steam Condenser Tube Conductivity"}}, {{
       Hold[$CellContext`lqSteamCondenserSurroundingTemp$$], 300, 
       "Exhaust Steam Condenser Surrounding Temperature"}}, {{
       Hold[$CellContext`condensateWaterTubeConductivity$$], 1, 
       "Condensate Water Exchanger Tube Conductivity"}}, {
      Hold[
       Button[
       "reset parameters", {$CellContext`preheated$$ = 
         True, $CellContext`closedLoop$$ = 
         True, $CellContext`msrBaseCoolingRate$$ = 
         480, $CellContext`msrCoolingEfficiency$$ = 
         1.87333, $CellContext`noHeaters$$ = 
         8, $CellContext`coolantTubeConductivity$$ = 
         1, $CellContext`waterTubeConductivity$$ = 
         1, $CellContext`hpSteamTubeConductivity$$ = 
         1, $CellContext`lqSteamCondenserConductivity$$ = 
         1, $CellContext`lqSteamCondenserSurroundingTemp$$ = 
         300, $CellContext`condensateWaterTubeConductivity$$ = 1}]], 
      Manipulate`Dump`ThisIsNotAControl}}, Typeset`size$$ = {
    577., {304., 12.}}, Typeset`update$$ = 0, Typeset`initDone$$, 
    Typeset`skipInitDone$$ = True, $CellContext`preheated$729$$ = 
    False, $CellContext`closedLoop$730$$ = False}, 
    DynamicBox[Manipulate`ManipulateBoxes[
     1, StandardForm, 
      "Variables" :> {$CellContext`closedLoop$$ = 
        True, $CellContext`condensateWaterTubeConductivity$$ = 
        1, $CellContext`coolantHeatMult$$ = 
        125, $CellContext`coolantPerRecipe$$ = 
        20, $CellContext`coolantTubeConductivity$$ = 
        1, $CellContext`hpSteamTubeConductivity$$ = 
        1, $CellContext`lqSteamCondenserConductivity$$ = 
        1, $CellContext`lqSteamCondenserSurroundingTemp$$ = 
        300, $CellContext`msrBaseCoolingRate$$ = 
        480, $CellContext`msrCoolingEfficiency$$ = 
        1.87333, $CellContext`noHeaters$$ = 8, $CellContext`preheated$$ = 
        True, $CellContext`waterTubeConductivity$$ = 1}, 
      "ControllerVariables" :> {
        Hold[$CellContext`preheated$$, $CellContext`preheated$729$$, False], 
        Hold[$CellContext`closedLoop$$, $CellContext`closedLoop$730$$, 
         False]}, 
      "OtherVariables" :> {
       Typeset`show$$, Typeset`bookmarkList$$, Typeset`bookmarkMode$$, 
        Typeset`animator$$, Typeset`animvar$$, Typeset`name$$, 
        Typeset`specs$$, Typeset`size$$, Typeset`update$$, Typeset`initDone$$,
         Typeset`skipInitDone$$}, "Body" :> 
      StringJoin["Hot Coolant <-> Water Contacts: ", 
        ToString[
         N[$CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`msrBaseCoolingRate$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], "\n", 
        "Hot Coolant <-> Water: Hot Coolant Input Rate: ", 
        ToString[
         N[
         Rational[
            1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
          32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
           2 $CellContext`waterTubeConductivity$$) 
          If[$CellContext`closedLoop$$, 1, 0]/(
          4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
          1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
          7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
           If[$CellContext`preheated$$, 16000, 32000])]], " mB/t", "\n", 
        "Hot Coolant <-> Water: Water Input Rate: ", 
        ToString[
         N[
         80000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`msrBaseCoolingRate$$ $CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], " mB/t", "\n", 
        "Hot Coolant <-> Water: HP Steam Total Loop Output Rate: ", 
        ToString[
         N[
         400000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], " mB/t", "\n", 
        "\n", "HP Steam <-> Water Contacts: ", 
        ToString[
         N[
         6400000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-2) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], "\n", 
        "HP Steam <-> Water: Steam Total Loop Output Rate: ", 
        ToString[
         N[
         1600000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], "\n", "\n", 
        "LQ Steam -> Condensate Water Contacts: ", 
        ToString[
         N[
         64000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ \
$CellContext`lqSteamCondenserConductivity$$^(-1) \
$CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))/(1 + 
          Log[350/$CellContext`lqSteamCondenserSurroundingTemp$$])]], "\n", 
        "LQ Steam -> Condensate Water: LQ Steam Input Rate: ", 
        ToString[
         N[
         3200000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], " mB/t", "\n", 
        "LQ Steam -> Condensate Water: Condensate Water Output Rate: ", 
        ToString[
         N[
         80000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], " mB/t", "\n", 
        "\n", "Condensate Water <-> Hot Coolant Contacts: ", 
        ToString[
         N[
         6400 $CellContext`condensateWaterTubeConductivity$$^(-1) \
$CellContext`coolantHeatMult$$ $CellContext`coolantPerRecipe$$^(-1) \
$CellContext`coolantTubeConductivity$$ $CellContext`hpSteamTubeConductivity$$ \
$CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ If[$CellContext`closedLoop$$, 1, 0] 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], "\n", 
        "Condensate Water <-> Hot Coolant: Hot Coolant Input Rate: ", 
        ToString[
         N[
         32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
           2 $CellContext`waterTubeConductivity$$) 
          If[$CellContext`closedLoop$$, 1, 0]/(
          4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
          1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
          7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
           If[$CellContext`preheated$$, 16000, 32000])]], " mB/t", "\n", "\n",
         "Base Power Produced: ", 
        ToString[
         N[
         6400000 $CellContext`coolantHeatMult$$ \
$CellContext`coolantPerRecipe$$^(-1) $CellContext`coolantTubeConductivity$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrBaseCoolingRate$$ (1 + 
           Rational[
             2, 7] $CellContext`waterTubeConductivity$$) \
$CellContext`waterTubeConductivity$$ 
          If[$CellContext`preheated$$, 800, 900]^(-1) 
          If[$CellContext`preheated$$, 16000, 32000]^(-1) (
           Rational[
             1, 20] $CellContext`coolantPerRecipe$$ \
$CellContext`msrCoolingEfficiency$$ $CellContext`noHeaters$$ - 
           
           32000 $CellContext`coolantPerRecipe$$ \
$CellContext`hpSteamTubeConductivity$$ $CellContext`msrCoolingEfficiency$$ \
$CellContext`noHeaters$$ $CellContext`waterTubeConductivity$$ (7 + 
            2 $CellContext`waterTubeConductivity$$) 
           If[$CellContext`closedLoop$$, 1, 0]/(
           4480000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$ + 
           1280000 $CellContext`hpSteamTubeConductivity$$ \
$CellContext`waterTubeConductivity$$^2 + 
           7 $CellContext`condensateWaterTubeConductivity$$ \
$CellContext`coolantTubeConductivity$$ If[$CellContext`preheated$$, 800, 900] 
            If[$CellContext`preheated$$, 16000, 32000]))]], " RF/t"], 
      "Specifications" :> {
        Style[
        "Configurable Constants", Bold, 
         Medium], {{$CellContext`coolantPerRecipe$$, 20, 
          "Coolant Per Recipe"}}, {{$CellContext`coolantHeatMult$$, 125, 
          "Coolant Heat Multiplier"}}, 
        Button[
        "reset configs", {$CellContext`coolantPerRecipe$$ = 
          20, $CellContext`coolantHeatMult$$ = 125}], Delimiter, 
        Style[
        "Design Parameters", Bold, 
         Medium], {{$CellContext`preheated$$, True, "Preheated Water"}, {
         True, False}}, {{$CellContext`closedLoop$$, True, 
          "Closed Water/Steam Loop"}, {
         True, False}}, {{$CellContext`msrBaseCoolingRate$$, 480, 
          "MSR Coolant: Base Cooling Rate"}}, \
{{$CellContext`msrCoolingEfficiency$$, 1.87333, 
          "MSR Coolant: Cooling Efficiency"}}, {{$CellContext`noHeaters$$, 8, 
          "MSR Number of Coolant Heaters"}}, \
{{$CellContext`coolantTubeConductivity$$, 1, 
          "Coolant Exchanger Tube Conductivity"}}, \
{{$CellContext`waterTubeConductivity$$, 1, 
          "Water Exchanger Tube Conductivity"}}, \
{{$CellContext`hpSteamTubeConductivity$$, 1, 
          "HP Steam Exchanger Tube Conductivity"}}, \
{{$CellContext`lqSteamCondenserConductivity$$, 1, 
          "Exhaust Steam Condenser Tube Conductivity"}}, \
{{$CellContext`lqSteamCondenserSurroundingTemp$$, 300, 
          "Exhaust Steam Condenser Surrounding Temperature"}}, \
{{$CellContext`condensateWaterTubeConductivity$$, 1, 
          "Condensate Water Exchanger Tube Conductivity"}}, 
        Button[
        "reset parameters", {$CellContext`preheated$$ = 
          True, $CellContext`closedLoop$$ = 
          True, $CellContext`msrBaseCoolingRate$$ = 
          480, $CellContext`msrCoolingEfficiency$$ = 
          1.87333, $CellContext`noHeaters$$ = 
          8, $CellContext`coolantTubeConductivity$$ = 
          1, $CellContext`waterTubeConductivity$$ = 
          1, $CellContext`hpSteamTubeConductivity$$ = 
          1, $CellContext`lqSteamCondenserConductivity$$ = 
          1, $CellContext`lqSteamCondenserSurroundingTemp$$ = 
          300, $CellContext`condensateWaterTubeConductivity$$ = 1}]}, 
      "Options" :> {}, "DefaultOptions" :> {}],
     ImageSizeCache->{1120., {205., 211.}},
     SingleEvaluation->True],
    Deinitialization:>None,
    DynamicModuleValues:>{},
    SynchronousInitialization->True,
    UndoTrackedVariables:>{Typeset`show$$, Typeset`bookmarkMode$$},
    UnsavedVariables:>{Typeset`initDone$$},
    UntrackedVariables:>{Typeset`size$$}], "Manipulate",
   Deployed->True,
   StripOnInput->False],
  Manipulate`InterpretManipulate[1]]], "Output"]
},
WindowSize->{1920, 997},
Visible->True,
ScrollingOptions->{"VerticalScrollRange"->Fit},
ShowCellBracket->Automatic,
CellContext->Notebook,
TrackCellChangeTimes->False,
FrontEndVersion->"10.2 for Microsoft Windows (64-bit) (July 7, 2015)",
StyleDefinitions->"Default.nb"
]
(* End of Notebook Content *)

(* Internal cache information *)
(*CellTagsOutline
CellTagsIndex->{}
*)
(*CellTagsIndex
CellTagsIndex->{}
*)
(*NotebookFileOutline
Notebook[{
Cell[1464, 33, 22734, 438, 433, "Output"]
}
]
*)

(* End of internal cache information *)

(* NotebookSignature XwDB6uB#ISb8ED1HCXCt1Doz *)
