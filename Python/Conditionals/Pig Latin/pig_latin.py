def translate(text):
    vowels = set('aeiou')  # 'y' is not included as a vowel
    words = text.lower().split()
    translated_words = []
    
    for word in words:
        if not word.isalpha():
            translated_words.append(word)
            continue
            
        # Rule 1: Starts with a vowel, 'xr', or 'yt'
        if word[0] in vowels or word.startswith(('xr', 'yt')):
            translated = word + 'ay'
        
        # Rule 3: Starts with zero or more consonants followed by 'qu'
        elif 'qu' in word:
            qu_index = word.find('qu')
            if all(c not in vowels for c in word[:qu_index]) and 'y' not in word[:qu_index]:
                translated = word[qu_index + 2:] + word[:qu_index + 2] + 'ay'
            else:
                # Find the first vowel, treating 'y' as a consonant if it's the first letter
                first_vowel_index = next((i for i, c in enumerate(word) if c in vowels or (c == 'y' and i > 0)), len(word))
                translated = word[first_vowel_index:] + word[:first_vowel_index] + 'ay'
        
        # Rule 4: Starts with one or more consonants (including 'y') followed by a vowel or 'y'
        elif word[0] not in vowels or word[0] == 'y':
            # Find the first vowel, treating 'y' as a consonant if it's the first letter
            first_vowel_index = next((i for i, c in enumerate(word) if c in vowels or (c == 'y' and i > 0)), len(word))
            translated = word[first_vowel_index:] + word[:first_vowel_index] + 'ay'
        
        # Rule 2: Starts with one or more consonants
        else:
            first_vowel_index = next((i for i, c in enumerate(word) if c in vowels), len(word))
            translated = word[first_vowel_index:] + word[:first_vowel_index] + 'ay'
        
        translated_words.append(translated)
    
    return ' '.join(translated_words)