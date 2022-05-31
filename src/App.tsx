import React from 'react';
import {SafeAreaView, StyleSheet} from 'react-native';
import OldModule from './OldModule';

const App = () => {
  return (
    <SafeAreaView style={styles.background}>
      <OldModule />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  background: {flex: 1},
});

export default App;
