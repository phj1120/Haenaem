package xyz.parkh.challenge.study;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ClassUtils;

import java.util.Collection;

public class SpringSecurityStudy {

    @Test
    public void isAssignable() {
        /**
         * 좌측이 우측의 부모인가?
         * A a = new B()
         */
        System.out.println("ClassUtils.isAssignable(People.class, Animal.class) = " + ClassUtils.isAssignable(People.class, Animal.class)); // false
        System.out.println("ClassUtils.isAssignable(Animal.class, People.class) = " + ClassUtils.isAssignable(Animal.class, People.class)); // true

        System.out.println("ClassUtils.isAssignable(Dog.class, Animal.class) = " + ClassUtils.isAssignable(Dog.class, Animal.class)); // false
        System.out.println("ClassUtils.isAssignable(Animal.class, Dog.class) = " + ClassUtils.isAssignable(Animal.class, Dog.class)); // true

        System.out.println("ClassUtils.isAssignable(Dog.class, People.class) = " + ClassUtils.isAssignable(Dog.class, People.class)); // false
        System.out.println("ClassUtils.isAssignable(People.class, Dog.class) = " + ClassUtils.isAssignable(People.class, Dog.class)); // false

        Animal animal = new Animal("h", 25);
        People people = new People(animal);
        Dog dog = new Dog(animal.name, animal.age);

        Animal animal1 = new People(animal);
        Animal animal2 = new Dog(animal.name, animal.age);

        // Animal 에는 Dog, People 의 정보가 다 없기 때문에 런타임 에러 발생
//        Dog dog1 = (Dog) new Animal(animal.name, animal.age);
//        People people1 = (People) new Animal(animal.name, animal.age);

        Animal animal3 = new Dog(animal.name, animal.age);
        Animal animal4 = new People(animal);

        Dog dog1 = (Dog) animal3;
        People people1 = (People) animal4;
    }


    static class People extends Animal {
        String job; // 직업

        public People(Animal animal) {
            super(animal.name, animal.age);
        }
    }

    static class Dog extends Animal {
        String breed; // 품종

        public Dog(String name, int age) {
            super(name, age);
        }
    }

    static class Animal {
        String name;
        int age;

        public Animal(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void isAssignableInAuthenticationPrincipalArgumentResolver() {
//        principal 이 null 이 아니고 A  가 User.class 의 부모가 아니면
//        if (principal != null && !ClassUtils.isAssignable(parameter.getParameterType(), principal.getClass())) {
//        예외 처리

        // 이게 true 가 되야 @AuthenticationPrincipal 으로 현재 로그인 된 사용자 정보 가져올 수 있음
//        ClassUtils.isAssignable(@AuthenticationPrincipal 이 붙어 있는 있는 객체, User.class));

        System.out.println("ClassUtils.isAssignable(UserDetails.class, User.class) = " + ClassUtils.isAssignable(UserDetails.class, User.class));

        // Account 가 User 부모여야 함
        // User 의 부모가 UserDetails 인데 그러면 Account 가 UserDetails 를 상속 받으면 부모가 될 수 있나? -> 아님
        // 어쩌라는거야
        System.out.println("ClassUtils.isAssignable(UserImpl.class, User.class) = " + ClassUtils.isAssignable(UserDetailImpl.class, User.class));
        System.out.println("ClassUtils.isAssignable(UserExt.class, User.class) = " + ClassUtils.isAssignable(UserExt.class, User.class));
    }

    static class UserExt extends User {
        public UserExt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }
    }

    static class UserDetailImpl implements UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }

    @Test
    public void hi() {
    }
}
