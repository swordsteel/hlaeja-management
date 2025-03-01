document.addEventListener('DOMContentLoaded', () => {

  const toggle = document.getElementById('menu-toggle');
  const menu = document.getElementById('dropdown-menu');

  toggle.addEventListener('click', () => {
    menu.classList.toggle('hidden');
  });

  document.addEventListener('click', (e) => {
    if (!toggle.contains(e.target) && !menu.contains(e.target)) {
      menu.classList.add('hidden');
    }
  });
});

function makeLocalTime(elements) {
  elements.forEach(element => {
    const utcTime = element.getAttribute('data-timestamp');
    if (utcTime) {
      element.textContent = new Intl.DateTimeFormat(
        'sv-SE',
        {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
          hour12: false,
          timeZoneName: 'shortOffset'
        }
      ).format(new Date(utcTime));
    }
  });
}

function passwordMatchCheck(passwordInput, confirmPasswordInput, matchMessage) {
  if (passwordInput && confirmPasswordInput && matchMessage) {
    function checkPasswordMatch() {
      const password = passwordInput.value;
      const confirmPassword = confirmPasswordInput.value;
      if (password === '' && confirmPassword === '') {
        matchMessage.textContent = '';
        matchMessage.classList.remove('text-green-600', 'text-red-600');
      } else if (password === confirmPassword) {
        matchMessage.textContent = 'Passwords match';
        matchMessage.classList.remove('text-red-600');
        matchMessage.classList.add('text-green-600');
      } else {
        matchMessage.textContent = 'Passwords do not match';
        matchMessage.classList.remove('text-green-600');
        matchMessage.classList.add('text-red-600');
      }
    }
    passwordInput.addEventListener('input', checkPasswordMatch);
    confirmPasswordInput.addEventListener('input', checkPasswordMatch);
  }
}
