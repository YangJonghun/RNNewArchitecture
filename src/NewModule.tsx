import React, {useRef, useState} from 'react';
import {View, Button, StyleSheet} from 'react-native';
import {AnswerModule} from './new-module/modules/NativeAnswerModule';
import BackgroundView, {
  BackgroundViewRef,
} from './new-module/components/BackgroundView';
import {getRandomColor} from './randomColorGen';

const NewModule = () => {
  const ref = useRef<BackgroundViewRef>(null);
  const [step, setStep] = useState(1);

  const onPress = () => {
    setStep(v => v + 1);
    const theAnswer = AnswerModule?.sendQuestion(
      'What number is in native code?',
    );
    console.log(`Answer: ${theAnswer}`);
    ref.current?.changeBackgroundColor(getRandomColor());
  };

  return (
    <View style={styles.block}>
      <Button onPress={onPress} title="new module" color="black" />
      <BackgroundView
        ref={ref}
        style={styles.nativeView}
        color="black"
        step={step}
        onChangeBackground={color =>
          console.log(`background color changed! = ${color}`)
        }
      />
    </View>
  );
};

const styles = StyleSheet.create({
  block: {flex: 1, justifyContent: 'center', alignItems: 'center'},
  nativeView: {
    marginTop: 10,
    width: 100,
    height: 100,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default NewModule;
