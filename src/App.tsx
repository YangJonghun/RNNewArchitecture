import React from 'react';
import {SafeAreaView, StyleSheet} from 'react-native';
import NewModule from './NewModule';
import OldModule from './OldModule';

const App = () => {
  return (
    <SafeAreaView style={styles.background}>
      <OldModule />
      <NewModule />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  background: {flex: 1},
});

export default App;
